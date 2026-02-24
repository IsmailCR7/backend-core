package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadByIdWhenLeadSaved() {

        Lead lead = new Lead("123", "test@mail.ru", "+7123", "Company", "NEW");

        repository.save(lead);

        Lead found = repository.findById("123");
        assertThat(found).isNotNull();
        assertThat(found.email()).isEqualTo("test@mail.ru");
    }

    @Test
    void shouldReturnNullWhenLeadNotFound() {

        Lead found = repository.findById("unknown");

        assertThat(found).isNull();
    }

    @Test
    void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
        Lead lead1 = new Lead("1", "a@mail.ru", "+71", "A", "NEW");
        Lead lead2 = new Lead("2", "b@mail.ru", "+72", "B", "NEW");
        Lead lead3 = new Lead("3", "c@mail.ru", "+73", "C", "NEW");

        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);

        List<Lead> allLeads = repository.findAll();

        assertThat(allLeads).hasSize(3);
        assertThat(allLeads).containsExactlyInAnyOrder(lead1, lead2, lead3);
    }

    @Test
    void shouldDeleteLeadWhenLeadExists() {
        Lead lead = new Lead("123", "test@mail.ru", "+7123", "Company", "NEW");
        repository.save(lead);

        repository.delete("123");

        assertThat(repository.findById("123")).isNull();
        assertThat(repository.size()).isZero();
    }

    @Test
    void shouldOverwriteLeadWhenSaveWithSameId() {
        Lead lead1 = new Lead("1", "first@mail.ru", "+71", "First", "NEW");
        repository.save(lead1);

        Lead lead2 = new Lead("1", "second@mail.ru", "+72", "Second", "HOT");
        repository.save(lead2);

        Lead found = repository.findById("1");
        assertThat(found.email()).isEqualTo("second@mail.ru");
        assertThat(repository.size()).isEqualTo(1); // размер не увеличился
    }
}