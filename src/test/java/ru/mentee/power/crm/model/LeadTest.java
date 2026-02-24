package ru.mentee.power.crm.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldBeEqualWhenSameId() {
        Lead lead1 = new Lead("123", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        Lead lead2 = new Lead("123", "petr@mail.ru", "+7987654", "Company B", "HOT");

        assertThat(lead1).isEqualTo(lead2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Lead lead1 = new Lead("123", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        Lead lead2 = new Lead("456", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        assertThat(lead1).isNotEqualTo(lead2);
    }

    @Test
    void shouldHaveSameHashCodeWhenSameId() {
        Lead lead1 = new Lead("123", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        Lead lead2 = new Lead("123", "petr@mail.ru", "+7987654", "Company B", "HOT");
        assertThat(lead1.hashCode()).isEqualTo(lead2.hashCode());
    }

    @Test
    void shouldWorkAsKeyInHashMapWhenUsingId() {
        Lead lead1 = new Lead("123", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        Lead lead2 = new Lead("123", "petr@mail.ru", "+7987654", "Company B", "HOT");

        Map<Lead, String> leadMap = new HashMap<>();
        leadMap.put(lead1, "Информация о лиде");
        assertThat(leadMap.get(lead2)).isEqualTo("Информация о лиде");
        assertThat(leadMap.size()).isEqualTo(1);
    }

    @Test
    void shouldWorkInHashSetWhenUsingId() {
        Lead lead1 = new Lead("123", "ivan@mail.ru", "+7123456", "Company A", "NEW");
        Lead lead2 = new Lead("123", "petr@mail.ru", "+7987654", "Company B", "HOT");

        Set<Lead> leadSet = new HashSet<>();
        leadSet.add(lead1);
        leadSet.add(lead2);

        assertThat(leadSet).hasSize(1);
        assertThat(leadSet).containsExactly(lead1);
    }

    @Test
    void shouldPreserveAllFieldsWhenCreated() {
        Lead lead = new Lead(
                "123",
                "test@mail.ru",
                "+79991234567",
                "ООО Ромашка",
                "NEW"
        );
        assertThat(lead.id()).isEqualTo("123");
        assertThat(lead.email()).isEqualTo("test@mail.ru");
        assertThat(lead.phone()).isEqualTo("+79991234567");
        assertThat(lead.company()).isEqualTo("ООО Ромашка");
        assertThat(lead.status()).isEqualTo("NEW");
    }

    @Test
    void shouldHaveCorrectToString() {
        Lead lead = new Lead("123", "test@mail.ru", "+7123", "Company", "NEW");
        String toString = lead.toString();

        assertThat(toString)
                .contains("123")
                .contains("test@mail.ru")
                .contains("+7123")
                .contains("Company")
                .contains("NEW");
    }
}