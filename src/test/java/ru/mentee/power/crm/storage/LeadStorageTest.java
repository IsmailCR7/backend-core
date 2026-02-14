package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class LeadStorageTest {
    @Test
    void shouldAddLeadWhenLeadIsUnique() {
        LeadStorage storage = new LeadStorage();
        Lead uniqueLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");


        boolean added = storage.add(uniqueLead);

        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicateWhenEmailAlreadyExists() {
        LeadStorage storage = new LeadStorage();
        Lead existingLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7456", "Other", "NEW");
        storage.add(existingLead);

        boolean added = storage.add(duplicateLead);

        assertThat(added).isFalse();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(existingLead);
    }

    @Test
    void shouldThrowExceptionWhenStorageIsFull() {
        LeadStorage storage = new LeadStorage();
        for (int index = 0; index < 100; index++) {
            storage.add(new Lead(UUID.randomUUID(), "lead" + index + "@mail.ru", "+7000", "Company", "NEW"));
        }
        Lead hundredFirstLead = new Lead(UUID.randomUUID(), "lead101@mail.ru", "+7001", "Company", "NEW");

        assertThatThrownBy(() -> storage.add(hundredFirstLead))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Storage is full");
    }

    @Test
    void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
        LeadStorage storage = new LeadStorage();
        Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        Lead secondLead = new Lead(UUID.randomUUID(), "maria@startup.io", "+7456", "StartupLab", "NEW");
        storage.add(firstLead);
        storage.add(secondLead);

        Lead[] result = storage.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(firstLead, secondLead);
    }
    @Test
    void shouldNotAddDuplicateLeadWithNullEmail() {
        // Arrange
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail1 = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead leadWithNullEmail2 = new Lead(UUID.randomUUID(), null, "+7001", "Company", "NEW");

        // Act
        boolean firstAddition = storage.add(leadWithNullEmail1);
        boolean secondAddition = storage.add(leadWithNullEmail2);

        // Assert
        assertThat(firstAddition).isTrue();           // Первый null email добавляется
        assertThat(secondAddition).isFalse();         // Второй null email не добавляется (дубликат)
        assertThat(storage.size()).isEqualTo(1);      // В хранилище только один лид
    }

    @Test
    void shouldHandleLeadsWithNullAndNonNullEmail() {
        // Arrange
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead leadWithEmail = new Lead(UUID.randomUUID(), "test@mail.ru", "+7001", "Company", "NEW");
        Lead anotherLeadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7002", "Company", "NEW");

        // Act
        boolean addNull1 = storage.add(leadWithNullEmail);
        boolean addWithEmail = storage.add(leadWithEmail);
        boolean addNull2 = storage.add(anotherLeadWithNullEmail);

        // Assert
        assertThat(addNull1).isTrue();
        assertThat(addWithEmail).isTrue();
        assertThat(addNull2).isFalse();               // Должен быть дубликатом (email = null)
        assertThat(storage.size()).isEqualTo(2);      // Должно быть 2 лида: один с null, один с email
    }

    @Test
    void shouldNotThrowNPEWhenAddingLeadWithNullEmail() {
        // Arrange
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");

        // Act & Assert
        assertThatCode(() -> storage.add(leadWithNullEmail))
                .doesNotThrowAnyException();          // Не должно быть NPE
    }

    @Test
    void shouldTreatTwoNullEmailsAsDuplicates() {
        // Arrange
        LeadStorage storage = new LeadStorage();
        Lead lead1 = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), null, "+7001", "Company", "NEW");
        Lead lead3 = new Lead(UUID.randomUUID(), "test@mail.ru", "+7002", "Company", "NEW");
        Lead lead4 = new Lead(UUID.randomUUID(), null, "+7003", "Company", "NEW");

        // Act
        storage.add(lead1);  // null email
        storage.add(lead2);  // null email - дубликат
        storage.add(lead3);  // с email
        storage.add(lead4);  // null email - дубликат

        // Assert
        assertThat(storage.size()).isEqualTo(2);      // Только lead1 и lead3
        Lead[] allLeads = storage.findAll();
        assertThat(allLeads).containsExactlyInAnyOrder(lead1, lead3);
    }
}





