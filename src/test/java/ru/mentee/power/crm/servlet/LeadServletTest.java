package ru.mentee.power.crm.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

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

    private LeadListServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LeadListServlet();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        servlet.init(servletConfig);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
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

        // Выполняем метод
        servlet.doGet(request, response);

        // Проверяем, что установлен правильный Content-Type
        verify(response).setContentType("text/html; charset=UTF-8");


        verify(leadService).findAll();


        String html = stringWriter.toString();


        assertTrue(html.contains("<!DOCTYPE html>"), "Должен быть DOCTYPE");
        assertTrue(html.contains("<title>Lead List</title>"), "Должен быть title");
        assertTrue(html.contains("<h1>Lead List</h1>"), "Должен быть заголовок");
        assertTrue(html.contains("<table>"), "Должна быть таблица");

        assertTrue(html.contains("<th>Email</th>"), "Должна быть колонка Email");
        assertTrue(html.contains("<th>Company</th>"), "Должна быть колонка Company");
        assertTrue(html.contains("<th>Status</th>"), "Должна быть колонка Status");


        assertTrue(html.contains("ivan@example.com"), "Должен быть email Ивана");
        assertTrue(html.contains("ООО Ромашка"), "Должна быть компания Ромашка");
        assertTrue(html.contains("NEW"), "Должен быть статус NEW");

        assertTrue(html.contains("petr@example.com"), "Должен быть email Петра");
        assertTrue(html.contains("ЗАО ТехноСервис"), "Должна быть компания ТехноСервис");
        assertTrue(html.contains("CONTACTED"), "Должен быть статус CONTACTED");

        System.out.println("✅ Тест 1 пройден: сервлет корректно отображает лидов");
    }

    @Test
    @DisplayName("Тест 2: Должен обработать случай с пустым списком лидов")
    void shouldHandleEmptyLeadList() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        String html = stringWriter.toString();

        assertTrue(html.contains("Нет данных") || html.contains("colspan='3'"),
                "Должно быть сообщение об отсутствии данных");

        System.out.println("✅ Тест 2 пройден: сервлет корректно обрабатывает пустой список");
    }




    @Test
    @DisplayName("Тест 6: Должен сгенерировать правильную структуру HTML")
    void shouldGenerateCorrectHtmlStructure() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        String html = stringWriter.toString();

        assertTrue(html.startsWith("<!DOCTYPE html>"),
                "HTML должен начинаться с DOCTYPE");
        assertTrue(html.contains("<html>"),
                "Должен быть открывающий тег html");
        assertTrue(html.contains("</html>"),
                "Должен быть закрывающий тег html");
        assertTrue(html.contains("<head>"),
                "Должен быть head");
        assertTrue(html.contains("</head>"),
                "Должен быть закрывающий head");
        assertTrue(html.contains("<body>"),
                "Должен быть body");
        assertTrue(html.contains("</body>"),
                "Должен быть закрывающий body");

        System.out.println("✅ Тест 6 пройден: структура HTML корректна");
    }

    @Test
    @DisplayName("Тест 7: Должен установить правильную кодировку")
    void shouldSetCorrectEncoding() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(leadService.findAll()).thenReturn(List.of());

        servlet.doGet(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
    }
}