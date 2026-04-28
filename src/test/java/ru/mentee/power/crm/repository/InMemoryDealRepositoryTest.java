package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Deal;
import ru.mentee.power.crm.model.DealStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDealRepositoryTest {

    private InMemoryDealRepository repository;
    private Deal testDeal1;
    private Deal testDeal2;
    private UUID leadId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDealRepository();
        leadId = UUID.randomUUID();
        testDeal1 = new Deal(leadId, new BigDecimal("10000.00"));
        testDeal2 = new Deal(leadId, new BigDecimal("25000.00"));
    }

    @Test
    void saveShouldAddDealToStorage() {
        // When
        repository.save(testDeal1);

        // Then
        Optional<Deal> found = repository.findById(testDeal1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualTo(new BigDecimal("10000.00"));
    }

    @Test
    void saveShouldUpdateExistingDeal() {
        // Given
        repository.save(testDeal1);
        Deal updatedDeal = new Deal(testDeal1.getId(), leadId, new BigDecimal("15000.00"), DealStatus.NEW, testDeal1.getCreatedAt());

        // When
        repository.save(updatedDeal);

        // Then
        Optional<Deal> found = repository.findById(testDeal1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualTo(new BigDecimal("15000.00"));
    }

    @Test
    void saveMultipleDealsShouldStoreAll() {
        // When
        repository.save(testDeal1);
        repository.save(testDeal2);

        // Then
        List<Deal> all = repository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).containsExactlyInAnyOrder(testDeal1, testDeal2);
    }

    @Test
    void findByIdShouldReturnDealWhenExists() {
        // Given
        repository.save(testDeal1);

        // When
        Optional<Deal> result = repository.findById(testDeal1.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testDeal1.getId());
        assertThat(result.get().getAmount()).isEqualTo(testDeal1.getAmount());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotExists() {
        // When
        Optional<Deal> result = repository.findById(UUID.randomUUID());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByIdShouldReturnEmptyWhenStorageIsEmpty() {
        // When
        Optional<Deal> result = repository.findById(UUID.randomUUID());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoDeals() {
        // When
        List<Deal> result = repository.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findAllShouldReturnAllDeals() {
        // Given
        repository.save(testDeal1);
        repository.save(testDeal2);

        // When
        List<Deal> result = repository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testDeal1, testDeal2);
    }

    @Test
    void findAllShouldReturnListCopyNotAffectedByOriginal() {
        // Given
        repository.save(testDeal1);
        List<Deal> result = repository.findAll();

        // When
        result.clear();

        // Then
        assertThat(repository.findAll()).hasSize(1);
    }


    @Test
    void findByStatusShouldReturnEmptyListWhenNoDealsWithStatus() {
        // Given
        repository.save(testDeal1); // NEW

        // When
        List<Deal> result = repository.findByStatus(DealStatus.WON);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByStatusShouldReturnEmptyListWhenStorageIsEmpty() {
        // When
        List<Deal> result = repository.findByStatus(DealStatus.NEW);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteByIdShouldRemoveDealWhenExists() {
        // Given
        repository.save(testDeal1);
        assertThat(repository.findById(testDeal1.getId())).isPresent();

        // When
        repository.deleteById(testDeal1.getId());

        // Then
        assertThat(repository.findById(testDeal1.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteByIdShouldDoNothingWhenDealNotExists() {
        // Given
        repository.save(testDeal1);
        UUID nonExistentId = UUID.randomUUID();

        // When
        repository.deleteById(nonExistentId);

        // Then
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(testDeal1.getId())).isPresent();
    }

    @Test
    void deleteByIdShouldDoNothingWhenStorageIsEmpty() {
        // When
        repository.deleteById(UUID.randomUUID());

        // Then - не должно быть исключений
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void saveAllAndFindAllShouldWorkCorrectly() {
        // Given
        List<Deal> deals = List.of(testDeal1, testDeal2);

        // When
        deals.forEach(repository::save);

        // Then
        assertThat(repository.findAll()).hasSize(2);
        assertThat(repository.findById(testDeal1.getId())).isPresent();
        assertThat(repository.findById(testDeal2.getId())).isPresent();
    }

    @Test
    void findByStatusAfterStatusTransitionShouldReturnCorrectDeals() {
        // Given
        repository.save(testDeal1); // NEW
        assertThat(repository.findByStatus(DealStatus.NEW)).hasSize(1);

        // When
        testDeal1.transitionTo(DealStatus.QUALIFIED);
        repository.save(testDeal1);

        // Then
        assertThat(repository.findByStatus(DealStatus.NEW)).isEmpty();
        assertThat(repository.findByStatus(DealStatus.QUALIFIED)).hasSize(1);
    }
}
