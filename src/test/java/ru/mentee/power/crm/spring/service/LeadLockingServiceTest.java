package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private static final int TIMEOUT_SECONDS = 10;
    private static final int THREAD_POOL_SIZE = 2;

    @Autowired
    private LeadLockingService leadLockingService;

    @Autowired
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
    }

    @Test
    void shouldPreventLostUpdateWhenPessimisticLockUsed() throws Exception {
        // Given
        Lead lead = createAndSaveLead("concurrent@test.com", LeadStatus.NEW);
        UUID leadId = lead.getId();

        // When
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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

        // Then
        boolean completed = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        String status1 = task1.get();
        String status2 = task2.get();

        assertThat(status1).isIn("CONTACTED", "QUALIFIED");
        assertThat(status2).isIn("CONTACTED", "QUALIFIED");
        assertThat(status1).isNotEqualTo(status2);

        Lead finalLead = leadRepository.findById(leadId).orElseThrow();
        assertThat(finalLead.getStatus().toString()).isIn("CONTACTED", "QUALIFIED");

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenConcurrentUpdateWithoutLock() throws Exception {
        // Given
        Lead lead = createAndSaveLead("optimistic@test.com", LeadStatus.NEW);
        UUID leadId = lead.getId();

        // When
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> task1 = executor.submit(() -> {
            startLatch.await();
            leadLockingService.updateLeadStatusOptimistic(leadId, "CONTACTED");
            return null;
        });

        Future<?> task2 = executor.submit(() -> {
            startLatch.await();
            Thread.sleep(50);
            leadLockingService.updateLeadStatusOptimistic(leadId, "QUALIFIED");
            return null;
        });

        startLatch.countDown();

        // Then
        AtomicReference<Boolean> exceptionThrown = new AtomicReference<>(false);

        try {
            task1.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            task2.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            assertThat(e.getCause())
                    .isInstanceOfAny(ObjectOptimisticLockingFailureException.class);
            exceptionThrown.set(true);
        }

        assertThat(exceptionThrown.get()).isTrue();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private Lead createAndSaveLead(String email, LeadStatus status) {
        Lead lead = new Lead("Patrick", email, "TestCorp", status);
        return leadRepository.save(lead);
    }
}