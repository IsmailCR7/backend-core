package ru.mentee.power.crm.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {
    private LeadRepository leadRepository;
    private UUID testId;
    private Contact testContact;
    private Address testAddress;
    private Lead testLead;

    @BeforeEach
    void setUp(){
        leadRepository = new LeadRepository();

        testId = UUID.randomUUID();
        testAddress = new Address("Moscow", "Lenina", "123");
        testContact = new Contact("test@mail.ru", "+7999999", testAddress);
        testLead = new Lead(testId, testContact, "testCompany", "NEW");
    }

    @Test
    @DisplayName("Should automatically deduplicate leads by id")
    void shouldDeduplicateLeadsById() {
        Lead deduplicate = new Lead(testId, testContact, "testCompany", "NEW");
        boolean firstAddResult = leadRepository.add(testLead);
        boolean secondAddResult = leadRepository.add(deduplicate);

        assertThat(firstAddResult).isTrue();
        assertThat(secondAddResult).isFalse();
        assertThat(leadRepository.size()).isEqualTo(1);

    }
    @Test
    @DisplayName("Must count leads with different IDs as different (even if the other fields match)")
    void shouldTreatDifferentIdsAsDifferentLeads() {
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        Lead leadOne = new Lead(idOne, testContact, "ТестКомпания", "NEW");
        Lead leadTwo = new Lead(idTwo, testContact, "ТестКомпания", "NEW");
        boolean resultOne = leadRepository.add(leadOne);
        boolean resultTwo = leadRepository.add(leadTwo);
        assertThat(resultOne).isTrue();
        assertThat(resultTwo).isTrue();

        assertThat(leadRepository.size()).isEqualTo(2);

    }
    @Test
    @DisplayName("Should find existing lead through contains")
    void shouldFindExistingLead() {
        leadRepository.add(testLead);

        boolean result = leadRepository.contains(testLead);

        assertThat(result).isTrue();

    }
    @Test
    @DisplayName("Should return unmodifiable set from findAll")
    void shouldReturnUnmodifiableSet() {
        leadRepository.add(testLead);
        Set<Lead> foundLead = leadRepository.findAll();
         assertThatThrownBy(() -> foundLead.add(new Lead(UUID.randomUUID(), testContact, "НоваяКомпания", "NEW")
         )).isInstanceOf(UnsupportedOperationException.class);
         assertThat(foundLead).contains(testLead);
         assertThat(foundLead).hasSize(1);
    }
    @Test
    @DisplayName("Should perform contains() faster than ArrayList")
    void shouldPerformFasterThanArrayList() {
        int numberOfLeads = 10000;
        int numberOfChecks = 1000;

        Set<Lead> hashSet = new HashSet<>();
        List<Lead> arrayList = new ArrayList<>();
        for (int i = 0; i < numberOfLeads; i++) {
            Lead lead = new Lead(UUID.randomUUID(),
                    new Contact("user" + i + "@email.com", "+7999000" + String.format("%04d", i), testAddress),
                    "Компания" + i,
                    i % 2 == 0 ? "NEW" : "QUALIFIED"
            );
            hashSet.add(lead);
            arrayList.add(lead);

        }
        Lead searchLead = new Lead (
                UUID.randomUUID(),
                new Contact("search@email.com", "+79999999999", testAddress),
                "ПоискКомпания",
                "NEW"
        );
        long hashSetStart = System.nanoTime();
        for (int i = 0; i < numberOfChecks; i++) {
            hashSet.contains(searchLead);

        }
        long hashSetDuration = System.nanoTime() - hashSetStart;
        long arrayListStart = System.nanoTime();
        for (int i = 0; i < numberOfChecks; i++) {
            arrayList.contains(searchLead);
        }
        long arrayListDuration = System.nanoTime() - arrayListStart;


        System.out.println("HashSet время: " + hashSetDuration + " ns");
        System.out.println("ArrayList время: " + arrayListDuration + " ns");
        System.out.println("Соотношение (ArrayList/HashSet): " +
                (arrayListDuration / (double) hashSetDuration));

        assertThat(arrayListDuration).isGreaterThan(hashSetDuration * 100);
    }



}
