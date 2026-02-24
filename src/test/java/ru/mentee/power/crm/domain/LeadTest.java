package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeadTest {

    @Test
    void shouldCreateLeadWhenValidData() {
        Address address = new Address("Москва", "Тверская", "101000");
        Contact contact = new Contact("dav@mail.ru", "+79992404769", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "ООО Ромашка", "NEW");
        assertThat(lead.contact())
                .isNotNull()
                .isEqualTo(contact);
        assertThat(lead.id()).isEqualTo(id);
        assertThat(lead.company()).isEqualTo("ООО Ромашка");
        assertThat(lead.status()).isEqualTo("NEW");
    }

    @Test
    void shouldAccessEmailThroughDelegationWhenLeadCreated() {
        Address address = new Address("Санкт-Петербург", "Невский пр.", "191186");
        Contact contact = new Contact("petr@mail.ru", "+7 812 123-45-67", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "ООО Пример", "QUALIFIED");

        String email = lead.contact().email();

        assertThat(email).isEqualTo("petr@mail.ru");
        String city = lead.contact().address().city();
        assertThat(city).isEqualTo("Санкт-Петербург");
        assertThat(lead.contact().address().street())
                .isEqualTo("Невский пр.");
        assertThat(lead.contact().address().zip())
                .isEqualTo("191186");
    }

    @Test
    void shouldBeEqualWhenSameIdButDifferentContact() {
        Address addressFirst = new Address("Москва", "Тверская", "101000");
        Contact contactFirst = new Contact("ivan@mail.ru", "+7 999 123-45-67", addressFirst);

        Address addressSecond = new Address("СПб", "Невский", "191186");
        Contact contactSecond = new Contact("petr@mail.ru", "+7 812 123-45-67", addressSecond);
        UUID sameId = UUID.randomUUID();

        Lead leadFirst = new Lead(sameId, contactFirst, "ООО Ромашка", "NEW");
        Lead leadSecond = new Lead(sameId, contactSecond, "Другая компания", "QUALIFIED");

        assertThat(leadFirst).isEqualTo(leadSecond);
        assertThat(leadFirst.id()).isEqualTo(leadSecond.id());
    }

    @Test
    void shouldCompareLeadsByIdWhenUsingCustomMethod() {
        UUID sameId = UUID.randomUUID();

        Lead lead1 = new Lead(sameId,
                new Contact("ivan@mail.ru", "+7 999 123-45-67",
                        new Address("Москва", "Тверская", "101000")),
                "ООО Ромашка", "NEW");

        Lead lead2 = new Lead(sameId,
                new Contact("petr@mail.ru", "+7 812 123-45-67",
                        new Address("СПб", "Невский", "191186")),
                "Другая компания", "QUALIFIED");

        assertThat(lead1.id()).isEqualTo(lead2.id());
        assertThat(lead1).isEqualTo(lead2);
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        // Проверяем что создание Lead с contact=null бросает IllegalArgumentException
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() ->
                new Lead(id, null, "ООО Ромашка", "NEW")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Contact не может быть null");

        // Также проверяем другие обязательные поля
        assertThatThrownBy(() ->
                new Lead(null,
                        new Contact( "ivan@mail.ru", "+7 999 123-45-67",
                                new Address("Москва", "Тверская", "101000")),
                        "ООО Ромашка", "NEW")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID не может быть null");

        assertThatThrownBy(() ->
                new Lead(UUID.randomUUID(),
                        new Contact( "ivan@mail.ru", "+7 999 123-45-67",
                                new Address("Москва", "Тверская", "101000")),
                        "ООО Ромашка", null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Status не может быть null");
    }



    @Test
    void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {

        Address address = new Address("Казань", "Баумана", "420111");
        Contact contact = new Contact("marat@mail.ru", "+7 843 123-45-67", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "ООО Казань", "NEW");
        Contact contactFromLead = lead.contact();
        assertThat(contactFromLead).isEqualTo(contact);
        Address addressFromContact = contactFromLead.address();
        assertThat(addressFromContact).isEqualTo(address);
        String city = addressFromContact.city();
        assertThat(city).isEqualTo("Казань");
        String cityDirect = lead.contact().address().city();
        assertThat(cityDirect).isEqualTo("Казань");
        assertThat(lead.contact().address().street()).isEqualTo("Баумана");
        assertThat(lead.contact().address().zip()).isEqualTo("420111");
        assertThat(lead.contact().email()).isEqualTo("marat@mail.ru");
    }

    @Test
    void shouldCreateNewLeadWhenChangingStatus() {
        Lead lead = new Lead(UUID.randomUUID(),
                new Contact("ivan@mail.ru", "+7 999 123-45-67",
                        new Address("Москва", "Тверская", "101000")),
                "ООО Ромашка", "NEW");
        Lead qualifiedLead = new Lead(
                lead.id(),
                lead.contact(),
                lead.company(),
                "QUALIFIED"
        );

        assertThat(lead.status()).isEqualTo("NEW");
        assertThat(qualifiedLead.status()).isEqualTo("QUALIFIED");
        assertThat(lead.id()).isEqualTo(qualifiedLead.id()); // ID одинаковый
        assertThat(lead.contact()).isEqualTo(qualifiedLead.contact()); // Contact тот же
    }

}