package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadEqualsHashCodeTest {

    @Test
    void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(),new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        // Then: Объект равен сам себе (isEqualTo использует equals() внутри)
        assertThat(lead).isEqualTo(lead);
    }

    @Test
    void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
        UUID uuid = UUID.randomUUID();
        Lead firstLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead secondLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")) , "TechCorp", "NEW");

        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(firstLead);
    }

    @Test
    void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
        UUID uuid = UUID.randomUUID();
        Lead firstLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead secondLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead thirdLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67", new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");


        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(secondLead).isEqualTo(thirdLead);
        assertThat(firstLead).isEqualTo(thirdLead);
    }

    @Test
    void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
        UUID uuid = UUID.randomUUID();

        Lead firstLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead secondLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead).isEqualTo(secondLead);
    }

    @Test
    void shouldReturnFalseWhenEqualsComparedWithNull() {

        Lead lead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        assertThat(lead).isNotEqualTo(null);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        UUID uuid = UUID.randomUUID();
        Lead firstLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead secondLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
    }

    @Test
    void shouldWorkInHashMapWhenLeadUsedAsKey() {
        UUID uuid = UUID.randomUUID();
        Lead keyLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead lookupLead = new Lead(uuid, new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        Map<Lead, String> map = new HashMap<>();
        map.put(keyLead, "CONTACTED");

        String status = map.get(lookupLead);


        assertThat(status).isEqualTo("CONTACTED");
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {

        Lead firstLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");
        Lead differentLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7 999 123-45-67",
                new Address("Москва", "Тверская", "101000")), "TechCorp", "NEW");

        assertThat(firstLead).isNotEqualTo(differentLead);
    }
}