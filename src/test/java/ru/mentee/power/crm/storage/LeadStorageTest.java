package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class LeadStorageTest {
    @Test
    void shouldAddLeadWhenLeadIsUnique() {
        LeadStorage storage = new LeadStorage();
        Lead uniqueLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");


        boolean added = storage.add(uniqueLead);

        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicateWhenEmailAlreadyExists() {
        LeadStorage storage = new LeadStorage();
        Lead existingLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "Other", "NEW");
        storage.add(existingLead);

        boolean added = storage.add(duplicateLead);

        assertThat(added).isFalse();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(existingLead);
    }



}




