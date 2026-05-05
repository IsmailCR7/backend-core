package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@SpringBootApplication(scanBasePackages = "ru.mentee.power.crm")
@EntityScan(basePackages = "ru.mentee.power.crm.model")
@EnableJpaRepositories(basePackages = "ru.mentee.power.crm.repository")
@Slf4j
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        LeadService leadService = context.getBean(LeadService.class);

        // Добавляем тестовые данные только если база пустая
        if (leadService.findAll().isEmpty()) {
            log.info("Добавление тестовых данных...");

            addLeadSafely(leadService, "davidov-ismail@mail.ru", "АК Победа", LeadStatus.QUALIFIED);
            addLeadSafely(leadService, "ivan@example.com", "ООО Ромашка", LeadStatus.NEW);
            addLeadSafely(leadService, "petr@example.com", "ЗАО ТехноСервис", LeadStatus.CONTACTED);
            addLeadSafely(leadService, "anna@example.com", "ИП Анна", LeadStatus.QUALIFIED);
            addLeadSafely(leadService, "sergey@example.com", "ООО СтройИнвест", LeadStatus.CONTACTED);
            addLeadSafely(leadService, "elena@example.com", "АО МедиаГрупп", LeadStatus.LOST);

            log.info("✅ Тестовые данные добавлены");
        } else {
            log.info("ℹ️ База уже содержит {} записей", leadService.findAll().size());
        }

        log.info("🚀 Приложение запущено на порту 8081");
        log.info("📊 Открыть: http://localhost:8081/leads");
    }

    private static void addLeadSafely(LeadService service, String email, String company, LeadStatus status) {
        try {
            service.addLead(email, company, status);
            log.info("✅ Добавлен: {} - {}", email, company);
        } catch (Exception e) {
            log.warn("⚠️ Не добавлен {}: {}", email, e.getMessage());
        }
    }
}