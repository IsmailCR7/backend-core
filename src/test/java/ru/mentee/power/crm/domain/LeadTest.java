package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldCreateLeadWhenValidData() {
        // Создаем UUID через UUID.randomUUID()
        UUID id = UUID.randomUUID();

        // Создаем Lead с этим UUID вместо String id
        Lead lead = new Lead(id, "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // Проверяем что lead.id() возвращает правильный UUID
        assertThat(lead.getId()).isEqualTo(id);
    }

    @Test
    void shouldGenerateUniqueIdsWhenMultipleLeads() {
        // Создаем два Lead с разными UUID.randomUUID()
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Lead lead1 = new Lead(id1, "lead1@example.com", "+1111111111", "Company A", "NEW");
        Lead lead2 = new Lead(id2, "lead2@example.com", "+2222222222", "Company B", "CONTACTED");

        // Проверяем что id разные
        assertThat(lead1.getId()).isNotEqualTo(lead2.getId());
    }

    @Test
    void shouldReturnIdWhenGetIdCalled() {
        UUID expectedId = UUID.randomUUID();
        Lead lead = new Lead(expectedId, "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        UUID id = lead.getId();
        assertThat(id).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnEmailWhenGetEmail() {
        Lead lead = new Lead(UUID.randomUUID(), "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String email = lead.getEmail();
        assertThat(email).isEqualTo("davidov-ismail@mail.ru");
    }

    @Test
    void shouldReturnPhoneWhenGetPhone() {
        Lead lead = new Lead(UUID.randomUUID(), "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String phone = lead.getPhone();
        assertThat(phone).isEqualTo("+79992404769");
    }

    @Test
    void shouldReturnCompanyWhenGetCompany() {
        Lead lead = new Lead(UUID.randomUUID(), "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String company = lead.getCompany();
        assertThat(company).isEqualTo("Pobeda");
    }

    @Test
    void shouldReturnStatusWhenGetStatus() {
        Lead lead = new Lead(UUID.randomUUID(), "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String status = lead.getStatus();
        assertThat(status).isEqualTo("flyAttendant");
    }

    @Test
    void shouldReturnFormattedStringWhenToStringCalled() {
        UUID id = UUID.randomUUID();
        String email = "davidov-ismail@mail.ru";
        String phone = "+79992404769";
        String company = "Pobeda";
        String status = "flyAttendant";

        Lead lead = new Lead(id, email, phone, company, status);
        String result = lead.toString();

        assertThat(result)
                .contains(id.toString())
                .contains(email)
                .contains(phone)
                .contains(company)
                .contains(status);
    }

    @Test
    void shouldPreventStringConfusionWhenUsingUUID() {
        // Демонстрация типобезопасности

        // Создаем Lead с UUID
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // Пример метода, который принимает UUID (для демонстрации)
        class LeadService {
            public Lead findById(UUID id) {
                // В реальном приложении здесь был бы поиск по ID
                return lead;
            }
        }

        LeadService service = new LeadService();

        // Это правильно - передаем UUID
        Lead foundLead = service.findById(lead.getId());
        assertThat(foundLead).isNotNull();

        // Демонстрация того, что передать String невозможно:
        // Следующая строка НЕ СКОМПИЛИРУЕТСЯ, если раскомментировать:
        // service.findById("some-string"); // Ошибка компиляции: incompatible types

        // Это демонстрирует преимущество UUID - нельзя случайно перепутать типы
    }

    // Вспомогательный тест для демонстрации, что разные UUID действительно разные
    @Test
    void shouldDemonstrateUuidUniqueness() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        assertThat(uuid1).isNotEqualTo(uuid2);
        assertThat(uuid1.toString()).isNotEqualTo(uuid2.toString());
    }
}