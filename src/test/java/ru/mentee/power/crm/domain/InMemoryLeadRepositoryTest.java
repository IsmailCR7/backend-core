package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLeadRepositoryTest {

    private InMemoryLeadRepository repository;
    private Lead testLead;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
        Contact contact = new Contact("john@example.com", "+79992404769", new Address("Saint-Peterburg", "Moskovskaya", "1234"));
        testLead = new Lead(UUID.randomUUID(), contact, "Company Inc", "NEW");
    }

    @Test
    void givenEmptyRepositoryWhenAddLeadThenFindAllReturnsOneElement() {
        // Given
        Lead firstLead = testLead;

        // When
        repository.add(firstLead);

        // Then
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(firstLead.id()))
                .isPresent()
                .contains(firstLead);
    }

    @Test
    void givenRepositoryWith10LeadsWhenFindByNonExistingIdThenReturnsEmptyOptional() {
        // Given
        for (int i = 1; i <= 10; i++) {
            Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");
            Contact contact = new Contact("davidov-smail@mail.ru", "+79992404769", address);
            Lead lead = new Lead(UUID.randomUUID(), contact, "Company " + i, "NEW");
            repository.add(lead);
        }

        // When
        UUID nonExistingId = UUID.randomUUID();
        Optional<Lead> result = repository.findById(nonExistingId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void givenRepositoryWithLeadWhenAddingDuplicateThenSizeRemainsOne() {
        // Given
        Lead firstLead = testLead;
        repository.add(firstLead);

        // When
        Lead duplicateLead = new Lead(firstLead.id(), firstLead.contact(),
                firstLead.company(), firstLead.status());
        repository.add(duplicateLead);

        // Then
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void givenRepositoryWith5LeadsWhenRemoveExistingLeadThenSizeBecomes4() {
        // Given
        List<Lead> leads = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");
            Contact contact = new Contact("davidov-smail@mail.ru", "+79992404769", address);
            Lead lead = new Lead(UUID.randomUUID(), contact, "Company " + i, "NEW");
            leads.add(lead);
            repository.add(lead);
        }

        UUID uuidToRemove = leads.get(0).id();

        // When
        repository.remove(uuidToRemove);

        // Then
        assertThat(repository.findAll()).hasSize(4);
        assertThat(repository.findById(uuidToRemove)).isEmpty();
    }

    @Test
    void givenRepositoryWithLeadsWhenClientModifiesFindAllResultThenInternalStorageUnaffected() {
        // Given
        repository.add(testLead);

        // When
        List<Lead> externalList = repository.findAll();
        externalList.clear(); // Пытаемся очистить полученный список

        // Then
        assertThat(repository.findAll()).isNotEmpty().hasSize(1);
        assertThat(repository.findById(testLead.id())).isPresent();
    }
}
