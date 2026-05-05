package ru.mentee.power.crm.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldCreateLeadWhenValidData() {
        // Given
        UUID id = UUID.randomUUID();
        String email = "example@gmail.com";
        String company = "TechCorp";
        LeadStatus status = LeadStatus.NEW;
        LocalDateTime createdAt = LocalDateTime.now();

        // When - используем @AllArgsConstructor (все поля включая id и createdAt)
        Lead lead = new Lead(id, email, company, status, createdAt);

        // Then
        assertThat(lead.getId()).isEqualTo(id);
        assertThat(lead.getEmail()).isEqualTo(email);
        assertThat(lead.getCompany()).isEqualTo(company);
        assertThat(lead.getStatus()).isEqualTo(status);
        assertThat(lead.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldCreateLeadWithGeneratedIdAndCreatedAt() {
        // When - используем кастомный конструктор (без id, без createdAt)
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);

        // Then
        assertThat(lead.getId()).isNull(); // ID будет сгенерирован при сохранении в БД
        assertThat(lead.getEmail()).isEqualTo("example@gmail.com");
        assertThat(lead.getCompany()).isEqualTo("TechCorp");
        assertThat(lead.getStatus()).isEqualTo(LeadStatus.NEW);
        assertThat(lead.getCreatedAt()).isNotNull(); // createdAt установлен автоматически
    }

    @Test
    void shouldBeEqualWhenSameId() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // @AllArgsConstructor - все поля
        Lead firstLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(id, "different@email.com", "Different Corp", LeadStatus.CONTACTED, now);

        // Then - equals сравнивает ВСЕ поля
        // ВНИМАНИЕ: разные email, company, status, но одинаковые id и createdAt → объекты НЕ равны!
        assertThat(firstLead).isNotEqualTo(secondLead); // потому что отличаются другие поля
    }

    @Test
    void shouldBeEqualWhenAllFieldsSame() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Lead firstLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.NEW, now);

        // Then - все поля одинаковы
        assertThat(firstLead).isEqualTo(secondLead);
        assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Lead firstLead = new Lead(UUID.randomUUID(), "example@gmail.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(UUID.randomUUID(), "example@gmail.com", "TechCorp", LeadStatus.NEW, now);

        // Then - разные ID → разные объекты
        assertThat(firstLead).isNotEqualTo(secondLead);
    }

    @Test
    void shouldNotBeEqualWhenDifferentEmail() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Lead firstLead = new Lead(id, "first@email.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(id, "second@email.com", "TechCorp", LeadStatus.NEW, now);

        // Then - разные email → разные объекты
        assertThat(firstLead).isNotEqualTo(secondLead);
    }

    @Test
    void shouldNotBeEqualWhenDifferentCompany() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Lead firstLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(id, "example@gmail.com", "AnotherCorp", LeadStatus.NEW, now);

        // Then - разные company → разные объекты
        assertThat(firstLead).isNotEqualTo(secondLead);
    }

    @Test
    void shouldNotBeEqualWhenDifferentStatus() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Lead firstLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.NEW, now);
        Lead secondLead = new Lead(id, "example@gmail.com", "TechCorp", LeadStatus.CONTACTED, now);

        // Then - разные status → разные объекты
        assertThat(firstLead).isNotEqualTo(secondLead);
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        // Given - используем @NoArgsConstructor
        Lead lead = new Lead();
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String company = "Test Corp";
        LeadStatus status = LeadStatus.QUALIFIED;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        lead.setId(id);
        lead.setEmail(email);
        lead.setCompany(company);
        lead.setStatus(status);
        lead.setCreatedAt(createdAt);

        // Then
        assertThat(lead.getId()).isEqualTo(id);
        assertThat(lead.getEmail()).isEqualTo(email);
        assertThat(lead.getCompany()).isEqualTo(company);
        assertThat(lead.getStatus()).isEqualTo(status);
        assertThat(lead.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldBeEqualWhenSameObject() {
        // Given
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);

        // Then
        assertThat(lead.equals(lead)).isTrue();
    }

    @Test
    void shouldNotBeEqualWhenNull() {
        // Given
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);

        // Then
        assertThat(lead.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqualWhenDifferentClass() {
        // Given
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);
        String notALead = "I am not a Lead";

        // Then
        assertThat(lead.equals(notALead)).isFalse();
    }

    @Test
    void toStringShouldContainAllFields() {
        // Given
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);

        // When
        String toStringResult = lead.toString();

        // Then
        assertThat(toStringResult).contains("example@gmail.com");
        assertThat(toStringResult).contains("TechCorp");
        assertThat(toStringResult).contains("NEW");
        assertThat(toStringResult).contains("createdAt");
    }
}