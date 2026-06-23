package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@SpringBootTest
@ActiveProfiles("test")
class LeadLockingServiceTest {

    @Autowired
    private LeadLockingService leadLockingService;

    @Autowired
    private LeadRepository leadRepository;

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
    }

    @Test
    void shouldPreventLostUpdateWhenPessimisticLockUsed() throws Exception {
        Lead lead = new Lead("Patrick", "concurrent@test.com", LeadStatus.NEW);
        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);

        Future<String> task1 = executor.submit(() -> {
            startLatch.await();
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, "CONTACTED");
            doneLatch.countDown();
            return updated.getStatus().toString();
        });

        Future<String> task2 = executor.submit(() -> {
            startLatch.await();
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, "QUALIFIED");
            doneLatch.countDown();
            return updated.getStatus().toString();
        });

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        String status1 = task1.get();
        String status2 = task2.get();

        assertThat(status1).isIn("CONTACTED", "QUALIFIED");
        assertThat(status2).isIn("CONTACTED", "QUALIFIED");
        assertThat(status1).isNotEqualTo(status2);

        Lead finalLead = leadRepository.findById(leadId).orElseThrow();
        assertThat(finalLead.getStatus().toString()).isIn("CONTACTED", "QUALIFIED");

        executor.shutdown();
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenConcurrentUpdateWithoutLock() throws Exception {
        // Given: Lead с optimistic locking через @Version
        Lead lead = new Lead("Patrick", "optimistic@test.com", LeadStatus.NEW);
        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        // When: Два потока одновременно обновляют БЕЗ pessimistic lock
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        // Используем AtomicReference для хранения исключений
        java.util.concurrent.atomic.AtomicReference<Exception> exception1 = new java.util.concurrent.atomic.AtomicReference<>();
        java.util.concurrent.atomic.AtomicReference<Exception> exception2 = new java.util.concurrent.atomic.AtomicReference<>();

        Future<?> task1 = executor.submit(() -> {
            try {
                startLatch.await();
                leadLockingService.updateLeadStatusOptimistic(leadId, "CONTACTED");
            } catch (Exception e) {
                exception1.set(e);
            }
            return null;
        });

        Future<?> task2 = executor.submit(() -> {
            try {
                startLatch.await();
                Thread.sleep(50); // Даем первому потоку начать
                leadLockingService.updateLeadStatusOptimistic(leadId, "QUALIFIED");
            } catch (Exception e) {
                exception2.set(e);
            }
            return null;
        });

        startLatch.countDown();

        // Ждем завершения задач
        task1.get(5, TimeUnit.SECONDS);
        task2.get(5, TimeUnit.SECONDS);

        executor.shutdown();

        // Then: Одно из исключений должно быть ObjectOptimisticLockingFailureException
        boolean hasOptimisticLockException = false;

        if (exception1.get() != null) {
            Throwable cause = exception1.get();
            // Проверяем, является ли исключение или его причина ObjectOptimisticLockingFailureException
            if (cause instanceof ObjectOptimisticLockingFailureException) {
                hasOptimisticLockException = true;
            } else if (cause.getCause() instanceof ObjectOptimisticLockingFailureException) {
                hasOptimisticLockException = true;
            }
        }

        if (exception2.get() != null && !hasOptimisticLockException) {
            Throwable cause = exception2.get();
            if (cause instanceof ObjectOptimisticLockingFailureException) {
                hasOptimisticLockException = true;
            } else if (cause.getCause() instanceof ObjectOptimisticLockingFailureException) {
                hasOptimisticLockException = true;
            }
        }

        assertThat(hasOptimisticLockException)
                .as("Expected ObjectOptimisticLockingFailureException but got: %s",
                        exception1.get() != null ? exception1.get().getClass().getSimpleName() : "null")
                .isTrue();
    }
}