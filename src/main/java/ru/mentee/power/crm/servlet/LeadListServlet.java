package ru.mentee.power.crm.servlet;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.PrintWriterOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        Path templatePath = Path.of("src/main/jte");
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
        this.templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LeadService service = (LeadService) getServletContext().getAttribute("leadService");

        String statusParam = request.getParameter("status");
        LeadStatus status = null;

        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                status = LeadStatus.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }

        List<Lead> leads;
        if (status == null) {
            leads = service.findAll();
        } else {
            leads = service.findByStatus(status);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("leads", leads);
        model.put("currentFilter", status);

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        PrintWriterOutput output = new PrintWriterOutput(writer);

        templateEngine.render("leads/list.jte", model, output);
    }
}