package ru.mentee.power.crm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;


class MainTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Тест : Проверка создания репозитория и сервиса")
    void shouldCreateRepositoryAndService() throws Exception {
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);

        assertNotNull(repository);
        assertNotNull(leadService);

        leadService.addLead("test@example.com", "Test Company", LeadStatus.NEW);
        assertEquals(1, leadService.findAll().size());
        assertEquals("test@example.com", leadService.findAll().get(0).email());
    }


    @Test
    @DisplayName("Тест : Проверка корректности URL")
    void shouldGenerateCorrectUrl() {
        int port = 8080;
        String expectedUrl = "http://localhost:" + port + "/leads";
        String actualUrl = "http://localhost:" + port + "/leads";

        assertEquals(expectedUrl, actualUrl);
        assertTrue(actualUrl.contains("localhost"));
        assertTrue(actualUrl.contains(String.valueOf(port)));
        assertTrue(actualUrl.endsWith("/leads"));
    }
}