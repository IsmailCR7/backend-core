package ru.mentee.power.crm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.spring.Application;

class StackComparisonTest {

    private static final int SERVLET_PORT = 8080;
    private static final int SPRING_PORT = 8081;

    private HttpClient httpClient;

    @Autowired
    private LeadService springLeadService;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }


    @Test
    @DisplayName("Измерение времени старта обоих стеков")
    void shouldMeasureStartupTime() throws LifecycleException {
        long servletStartupMs = measureServletStartup();

        long springStartupMs = measureSpringBootStartup();

        System.out.println("=== Сравнение времени старта ===");
        System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
        System.out.printf("Spring Boot: %d ms%n", springStartupMs);
        System.out.printf("Разница: Spring %s на %d ms%n",
                springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
                Math.abs(springStartupMs - servletStartupMs));

        assertThat(servletStartupMs).isLessThan(10_000);
        assertThat(springStartupMs).isLessThan(15_000);
    }

    private long measureServletStartup() throws LifecycleException {

        LeadRepository leadRepository = new InMemoryLeadRepository();

        LeadService leadService = new LeadService(leadRepository);

        long servletStart = System.nanoTime();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(0);

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        context.getServletContext().setAttribute("LeadService", leadService);

        tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
        context.addServletMappingDecoded("/leads", "LeadListServlet");

        tomcat.start();

        long servletEnd = System.nanoTime();

        tomcat.stop();

        return  (servletEnd - servletStart) / 1_000_000;
    }

    private long measureSpringBootStartup() {
        long springStart = System.nanoTime();

        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "0"));

        ConfigurableApplicationContext context = app.run();

        long springEnd = System.nanoTime();

        context.close();

        return  (springEnd - springStart) / 1_000_000;
    }
}
