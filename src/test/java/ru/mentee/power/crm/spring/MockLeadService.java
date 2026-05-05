package ru.mentee.power.crm.spring;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Этот класс для тестирования без Spring контекста
public class MockLeadService extends LeadService {

    public MockLeadService() {
        super(null); // Вызов конструктора родителя с null (только для тестов)
    }

    @Override
    public List<Lead> findAll() {
        LocalDateTime now = LocalDateTime.now();
        Lead lead1 = new Lead("test1@example.com", "Company 1", LeadStatus.NEW);
        lead1.setId(UUID.randomUUID());
        lead1.setCreatedAt(now);

        Lead lead2 = new Lead("test2@example.com", "Company 2", LeadStatus.CONTACTED);
        lead2.setId(UUID.randomUUID());
        lead2.setCreatedAt(now);

        return Arrays.asList(lead1, lead2);
    }
}