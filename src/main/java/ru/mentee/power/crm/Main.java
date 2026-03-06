package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.web.HelloCrmServer;

import java.io.File;

public class Main {
    static void main(String[] args) throws Exception {
        System.out.println("=== Launching CRM application ===");
        LeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);

        leadService.addLead("davidov-ismail@mail.ru", "АК Победа", LeadStatus.QUALIFIED);
        leadService.addLead("ivan@example.com", "ООО Ромашка", LeadStatus.NEW);
        leadService.addLead("petr@example.com", "ЗАО ТехноСервис", LeadStatus.CONTACTED);
        leadService.addLead("anna@example.com", "ИП Анна", LeadStatus.QUALIFIED);
        leadService.addLead("sergey@example.com", "ООО СтройИнвест", LeadStatus.CONTACTED);
        leadService.addLead("elena@example.com", "АО МедиаГрупп", LeadStatus.NEW);

        Tomcat tomcat = new Tomcat();

        int port = 8080;
        tomcat.setPort(port);
        tomcat.getConnector();
        System.out.println("Configuring Tomcat on the port " + port);

        String contextPath = "";
        String baseDir = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, baseDir);
        context.getServletContext().setAttribute("leadService", leadService);

        String servletName = "LeadListServlet";
        String urlPattern = "/leads";

        tomcat.addServlet(context, servletName, new LeadListServlet());
        context.addServletMappingDecoded(urlPattern, servletName);
        System.out.println("The servlet is registered at: " + urlPattern);

        tomcat.start();
        System.out.println("=================================");
        System.out.println("Tomcat has been successfully launched on the port " + port);
        System.out.println("Open in the browser: http://localhost:" + port + "/leads");
        System.out.println("=================================");

        tomcat.getServer().await();

        HelloCrmServer server = new HelloCrmServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            System.out.println("\n⚠️  Получен сигнал завершения...");
            server.stop();
        }));

        server.start();

        System.out.println("🔄 Сервер ожидает запросы...");
        Thread.currentThread().join();

    }
}
