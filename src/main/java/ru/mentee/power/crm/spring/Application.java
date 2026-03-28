package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.mentee.power.crm"})
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        LeadService leadService = context.getBean(LeadService.class);

        try {
            leadService.addLead("davidov-ismail@mail.ru", "АК Победа", LeadStatus.QUALIFIED);
            System.out.println("✅ Добавлен: davidov-ismail@mail.ru");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        try {
            leadService.addLead("ivan@example.com", "ООО Ромашка", LeadStatus.NEW);
            System.out.println("✅ Добавлен: ivan@example.com");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        try {
            leadService.addLead("petr@example.com", "ЗАО ТехноСервис", LeadStatus.CONTACTED);
            System.out.println("✅ Добавлен: petr@example.com");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        try {
            leadService.addLead("anna@example.com", "ИП Анна", LeadStatus.QUALIFIED);
            System.out.println("✅ Добавлен: anna@example.com");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        try {
            leadService.addLead("sergey@example.com", "ООО СтройИнвест", LeadStatus.CONTACTED);
            System.out.println("✅ Добавлен: sergey@example.com");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        try {
            leadService.addLead("elena@example.com", "АО МедиаГрупп", LeadStatus.NEW);
            System.out.println("✅ Добавлен: elena@example.com");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }

        System.out.println("🚀 Приложение запущено с тестовыми данными!");
    }

}