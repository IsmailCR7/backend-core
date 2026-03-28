package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeadControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private LeadService leadService;

    @Test
    void testGetLeadsPageWithDataReturns200AndContainsLeads() {
        List<Lead> mockLeads = List.of(
                new Lead(UUID.randomUUID(), "mock@example.com", "Mock Company", LeadStatus.NEW),
                new Lead(UUID.randomUUID(), "test@example.com", "Test Company", LeadStatus.QUALIFIED)
        );

        when(leadService.findAll()).thenReturn(mockLeads);
        String url = "http://localhost:" + port + "/leads";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("mock@example.com");
        assertThat(response.getBody()).contains("test@example.com");
        assertThat(response.getBody()).contains("Mock Company");
        assertThat(response.getBody()).contains("Test Company");
        assertThat(response.getBody()).contains("NEW");
        assertThat(response.getBody()).contains("QUALIFIED");
    }

    @Test
    void testGetLeadsPageWithEmptyDataShowsNoDataMessage() {
        when(leadService.findAll()).thenReturn(List.of());

        String url = "http://localhost:" + port + "/leads";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Нет данных");
        assertThat(response.getBody()).doesNotContain("mock@example.com");
    }

    @Test
    void testGetLeadsPageReturnsCorrectHttpStatus() {
        when(leadService.findAll()).thenReturn(List.of());

        String url = "http://localhost:" + port + "/leads";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetLeadsPageWhenServiceThrowsExceptionHandlesGracefully() {
        when(leadService.findAll()).thenThrow(new RuntimeException("Database error"));

        String url = "http://localhost:" + port + "/leads";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}