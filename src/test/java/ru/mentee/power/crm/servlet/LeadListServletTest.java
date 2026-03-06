package ru.mentee.power.crm.servlet;

import gg.jte.TemplateEngine;
import gg.jte.output.PrintWriterOutput;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private LeadService leadService;

    @Mock
    private TemplateEngine templateEngine;

    @Captor
    private ArgumentCaptor<Map<String, Object>> modelCaptor;

    @Captor
    private ArgumentCaptor<String> templateNameCaptor;

    @Captor
    private ArgumentCaptor<PrintWriterOutput> outputCaptor;

    private LeadListServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LeadListServlet();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        servlet.init(servletConfig);

        Field templateEngineField = LeadListServlet.class.getDeclaredField("templateEngine");
        templateEngineField.setAccessible(true);
        templateEngineField.set(servlet, templateEngine);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        lenient().when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("Тест 1: Должен получить LeadService из ServletContext и отобразить лидов")
    void shouldGetLeadServiceAndDisplayLeads() throws Exception {
        List<Lead> testLeads = Arrays.asList(
                new Lead("ivan@example.com", "ООО Ромашка", LeadStatus.NEW),
                new Lead("petr@example.com", "ЗАО ТехноСервис", LeadStatus.CONTACTED),
                new Lead("anna@example.com", "ИП Анна", LeadStatus.QUALIFIED)
        );

        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(testLeads);

        servlet.doGet(request, response);

        verify(servletContext).getAttribute("leadService");
        verify(leadService).findAll();
        verify(response).setContentType("text/html; charset=UTF-8");
        verify(response).getWriter();

        verify(templateEngine).render(
                eq("leads/list.jte"),
                modelCaptor.capture(),
                any(PrintWriterOutput.class)
        );

        Map<String, Object> capturedModel = modelCaptor.getValue();
        assertNotNull(capturedModel);
        assertTrue(capturedModel.containsKey("leads"));

        List<Lead> capturedLeads = (List<Lead>) capturedModel.get("leads");
        assertEquals(3, capturedLeads.size());
        assertEquals("ivan@example.com", capturedLeads.get(0).email());
    }

    @Test
    @DisplayName("Тест 2: Должен обработать случай с пустым списком лидов")
    void shouldHandleEmptyLeadList() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        verify(templateEngine).render(
                eq("leads/list.jte"),
                modelCaptor.capture(),
                any(PrintWriterOutput.class)
        );

        Map<String, Object> capturedModel = modelCaptor.getValue();
        List<Lead> capturedLeads = (List<Lead>) capturedModel.get("leads");
        assertTrue(capturedLeads.isEmpty());
    }

    @Test
    @DisplayName("Тест 3: Должен установить правильную кодировку")
    void shouldSetCorrectEncoding() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
    }

    @Test
    @DisplayName("Тест 4: Должен использовать правильное имя шаблона")
    void shouldUseCorrectTemplateName() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        verify(templateEngine).render(
                templateNameCaptor.capture(),
                anyMap(),
                any(PrintWriterOutput.class)
        );

        assertEquals("leads/list.jte", templateNameCaptor.getValue());
    }

    @Test
    @DisplayName("Тест 5: Должен создать PrintWriterOutput с правильным writer")
    void shouldCreatePrintWriterOutputWithCorrectWriter() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        verify(templateEngine).render(
                anyString(),
                anyMap(),
                outputCaptor.capture()
        );

        assertNotNull(outputCaptor.getValue());
    }

    @Test
    @DisplayName("Тест 6: Должен пробросить исключение при ошибке")
    void shouldThrowExceptionWhenErrorOccurs() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenThrow(new RuntimeException("Ошибка БД"));

        assertThrows(RuntimeException.class, () -> {
            servlet.doGet(request, response);
        });
    }


}