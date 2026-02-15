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
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail1 = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead leadWithNullEmail2 = new Lead(UUID.randomUUID(), null, "+7001", "Company", "NEW");

        boolean firstAddition = storage.add(leadWithNullEmail1);
        boolean secondAddition = storage.add(leadWithNullEmail2);

        assertThat(firstAddition).isTrue();
        assertThat(secondAddition).isFalse();
        assertThat(storage.size()).isEqualTo(1);
    }

    @Test
    void shouldHandleLeadsWithNullAndNonNullEmail() {
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead leadWithEmail = new Lead(UUID.randomUUID(), "test@mail.ru", "+7001", "Company", "NEW");
        Lead anotherLeadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7002", "Company", "NEW");

        boolean addNull1 = storage.add(leadWithNullEmail);
        boolean addWithEmail = storage.add(leadWithEmail);
        boolean addNull2 = storage.add(anotherLeadWithNullEmail);

        assertThat(addNull1).isTrue();
        assertThat(addWithEmail).isTrue();
        assertThat(addNull2).isFalse();
        assertThat(storage.size()).isEqualTo(2);
    }

    @Test
    void shouldNotThrowNPEWhenAddingLeadWithNullEmail() {
        LeadStorage storage = new LeadStorage();
        Lead leadWithNullEmail = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        assertThatCode(() -> storage.add(leadWithNullEmail))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldTreatTwoNullEmailsAsDuplicates() {

        LeadStorage storage = new LeadStorage();
        Lead leadFirst = new Lead(UUID.randomUUID(), null, "+7000", "Company", "NEW");
        Lead leadSecond = new Lead(UUID.randomUUID(), null, "+7001", "Company", "NEW");
        Lead leadThird = new Lead(UUID.randomUUID(), "test@mail.ru", "+7002", "Company", "NEW");
        Lead leadFourth = new Lead(UUID.randomUUID(), null, "+7003", "Company", "NEW");


        storage.add(leadFirst);
        storage.add(leadSecond);
        storage.add(leadThird);
        storage.add(leadFourth);

        assertThat(storage.size()).isEqualTo(2);
        Lead[] allLeads = storage.findAll();
        assertThat(allLeads).containsExactlyInAnyOrder(leadFirst, leadThird);
    }
}





