package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class InMemoryLeadRepositoryTest {
    private InMemoryLeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
    }

    @Test
    void shouldReturnAddedLeadAndHisLength() {
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);
        repository.save(lead);
        assertThat(repository.findAll().size()).isEqualTo(1);
        assertThat(repository.findById(lead.id())).isEqualTo(Optional.of(lead));
    }

    @Test
    void shouldReturnOptionalEmptyWhenTryToFindLeadByNonExistingId() {
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);

        repository.save(lead);
        assertThat(repository.findById(UUID.randomUUID())).isEqualTo(Optional.empty());
    }

    @Test
    void shouldAddLeadsWithSameId() {
        Lead lead = new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW);
        repository.save(lead);
        repository.save(new Lead(lead.id(), "example@gmail.com", "TechCorp", LeadStatus.NEW));
        assertThat(repository.findAll().size()).isEqualTo(1);
    }
    @Test
    void removedLeadShouldBeFindBy() {
        UUID fistLeadId = UUID.randomUUID();

        repository.save(new Lead(fistLeadId, "example@gmail.com", "TechCorp", LeadStatus.NEW));
        repository.save(new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW));

        assertThat(repository.findAll().size()).isEqualTo(2);
        repository.delete(fistLeadId);
        assertThat(repository.findAll().size()).isEqualTo(1);
    }
    @Test
    void weShouldNotBeAbleToChangeInternalStorageManipulatingWithRerunOfMethod() {
        repository.save(new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW));
        repository.save(new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW));

        List<Lead> leads = repository.findAll();

        leads.add(new Lead("example@gmail.com", "TechCorp", LeadStatus.NEW));
        assertThat(leads.size()).isEqualTo(3);
        assertThat(repository.findAll().size()).isEqualTo(2);
    }


}
