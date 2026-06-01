package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
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
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.CannotCreateTransactionException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@SpringBootTest
@ActiveProfiles("test")
class LeadDeadlockServiceTest {

    private static final int TIMEOUT_SECONDS = 5;
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
    void shouldThrowExceptionWhenDeadlock() throws Exception {
        // Given
        Lead firstLead = createAndSaveLead("optimistic@test.com", LeadStatus.NEW);
        UUID firstLeadId = firstLead.getId();

        Lead secondLead = createAndSaveLead("temperance@test.com", LeadStatus.NEW);
        UUID secondLeadId = secondLead.getId();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);

        // When: Первый поток блокирует firstLeadId -> secondLeadId
        Future<?> task1 = executor.submit(() -> {
            startLatch.await();
            leadLockingService.blockLeadsInOrder(firstLeadId, secondLeadId);
            return null;
        });

        // Второй поток блокирует secondLeadId -> firstLeadId (deadlock)
        Future<?> task2 = executor.submit(() -> {
            startLatch.await();
            leadLockingService.blockLeadsInOrder(secondLeadId, firstLeadId);
            return null;
        });

        startLatch.countDown();

        // Then: Одна из транзакций должна выбросить исключение при deadlock'е
        AtomicReference<Boolean> exceptionThrown = new AtomicReference<>(false);

        try {
            task1.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            task2.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            // В зависимости от БД может быть разные исключения:
            // - PessimisticLockingFailureException (Spring)
            // - CannotAcquireLockException (Oracle/PostgreSQL)
            // - CannotCreateTransactionException (H2/MySQL при deadlock)
            Throwable cause = e.getCause();
            assertThat(cause)
                    .isInstanceOfAny(
                            PessimisticLockingFailureException.class,
                            CannotAcquireLockException.class,
                            CannotCreateTransactionException.class
                    );
            exceptionThrown.set(true);
        }

        assertThat(exceptionThrown.get())
                .as("Deadlock должен вызвать исключение у одной из транзакций")
                .isTrue();

        executor.shutdown();
        executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private Lead createAndSaveLead(String email, LeadStatus status) {
        Lead lead = new Lead("Patrick", email, "TestCorp", status);
        return leadRepository.save(lead);
    }
}
