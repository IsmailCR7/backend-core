package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.DealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

@SpringBootTest
class LeadServiceTest {

    @Autowired
    private LeadService service;

    @Autowired
    private LeadRepository repository;

    @MockitoBean
    private DealRepository dealRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        doNothing().when(dealRepository).save(any());

        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead();
            lead.setName("Name" + i);
            lead.setEmail("lead" + i + "@example.com");
            lead.setCompany("Company " + i);
            lead.setStatus(LeadStatus.NEW);
            repository.save(lead);
        }
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void convertNewToContactedShouldUpdateMultipleLeads() {
        int updated = service.convertNewToContacted();

        assertThat(updated).isEqualTo(3);

        long contactedCount = repository.countByStatus(LeadStatus.CONTACTED);
        assertThat(contactedCount).isEqualTo(3);

        long newCount = repository.countByStatus(LeadStatus.NEW);
        assertThat(newCount).isEqualTo(0);
    }


    @Test
    void searchByCompanyShouldReturnPage() {
        Lead lead = new Lead();
        lead.setName("Name4");
        lead.setEmail("lead4@example.com");
        lead.setCompany("Company 1");
        lead.setStatus(LeadStatus.NEW);
        repository.save(lead);

        Page<Lead> result = service.searchByCompany("Company 1", 0, 5);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void getFirstPageShouldReturnFirstPage() {
        Page<Lead> result = service.getFirstPage(1);

        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    @Test
    void convertLeadToDealShouldCommitOnSuccess() {
        List<Lead> leads = service.findAll();
        assertThat(leads).isNotEmpty();

        Lead lead = leads.get(0);
        assertThat(lead.status()).isEqualTo(LeadStatus.NEW);

        service.convertLeadToDeal(lead.id(), BigDecimal.valueOf(10_000));

        Lead updatedLead = service.findById(lead.id()).get();
        assertThat(updatedLead.status()).isEqualTo(LeadStatus.CONTACTED);
    }

    @Test
    @Transactional
    void convertLeadToDealShouldRollbackOnConstraintViolation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.convertLeadToDeal(UUID.randomUUID(), BigDecimal.valueOf(10_000)));
        assertThat(exception.getMessage()).contains("Lead not found");
    }

    @Test
    void demonstrateSelfInvocationProblem() {
        List<LeadStatus> statusesBefore = service.findByStatus(LeadStatus.NEW).stream()
                .map(Lead::getStatus).collect(Collectors.toList());
        List<UUID> ids = new ArrayList<>();
        for (Lead lead : service.findAll()) {
            ids.add(lead.id());
        }
        ids.add(UUID.randomUUID());

        service.processLeadsWithInvocationProblem(ids);

        List<LeadStatus> statusesAfter = service.findByStatus(LeadStatus.NEW).stream()
                .map(Lead::getStatus).collect(Collectors.toList());

        assertThat(statusesBefore).isEqualTo(statusesAfter);
        assertThat(statusesAfter).hasSize(3);
    }

    @Test
    void processLeadsShouldIsolateTransactionsPerLead() {
        List<LeadStatus> statusesBefore = service.findByStatus(LeadStatus.NEW).stream()
                .map(Lead::getStatus).collect(Collectors.toList());
        List<UUID> ids = new ArrayList<>();
        for (Lead lead : service.findAll()) {
            ids.add(lead.id());
        }
        ids.add(UUID.randomUUID());

        String transactionName = service.processLeads(ids);

        List<LeadStatus> statusesAfter = service.findByStatus(LeadStatus.NEW).stream()
                .map(Lead::getStatus).collect(Collectors.toList());

        assertThat(transactionName).contains("LeadProcessor")
                .contains("processSingleLead");
        assertThat(statusesBefore).isNotEqualTo(statusesAfter);
        assertThat(statusesAfter).hasSize(0);
    }

    @Transactional
    @ParameterizedTest
    @EnumSource(value = Propagation.class, names = {"REQUIRED", "MANDATORY"})
    void testPropagation(Propagation propagation) {
        List<UUID> ids = new ArrayList<>();
        for (Lead lead : service.findAll()) {
            ids.add(lead.id());
        }

        switch (propagation) {
            case REQUIRED:
                assertThat(service.processLeadsWithRequires(ids))
                        .contains("testPropagation")
                        .doesNotContain("LeadProcessor")
                        .doesNotContain("processSingleLeadWithRequired");
                break;
            case MANDATORY:
                assertThat(service.processLeadsWithMandatory(ids))
                        .contains("testPropagation")
                        .doesNotContain("LeadProcessor")
                        .doesNotContain("processSingleLeadWithMandatory");
                break;
            default:
                break;
        }
    }

    @Test
    void testPropagationMandatoryMethodShouldThrowExceptionWithoutTransaction() {
        List<UUID> ids = new ArrayList<>();
        for (Lead lead : service.findAll()) {
            ids.add(lead.id());
        }
        ids.add(UUID.randomUUID());

        assertThrows(IllegalTransactionStateException.class, () ->
                service.processLeadsWithMandatory(ids));
    }

    @Test
    void isolationReadCommittedAllowsNonRepeatableRead() {
        Lead lead = new Lead();
        lead.setName("John");
        lead.setEmail("john@example.com");
        lead.setCompany("TestComp");
        lead.setStatus(LeadStatus.NEW);
        repository.save(lead);

        List<String> results = service.readThenWriteThenReadAgainWithReadCommitted(
                lead.getId(), "Jane");

        assertThat(results).containsExactly("John", "Jane");
    }
}