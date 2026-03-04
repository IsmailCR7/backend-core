package ru.mentee.power.crm.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LeadService leadService = (LeadService) getServletContext().getAttribute("leadService");
        List<Lead> leads = leadService.findAll();
        System.out.println("Найдено лидов: " + leads.size());

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        generateHtmlTable(writer, leads);

        System.out.println("✅ Response sent successfully");
        System.out.println("=== End of request ===");
    }

    private void generateHtmlTable(PrintWriter writer, List<Lead> leads) {
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("    <title>CRM - Список лидов</title>");
        writer.println("    <style>");
        writer.println("        table { border-collapse: collapse; width: 100%; }");
        writer.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        writer.println("        th { background-color: #f2f2f2; }");
        writer.println("        tr:hover { background-color: #f5f5f5; }");
        writer.println("    </style>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("    <h1>Список лидов</h1>");
        writer.println("    <table>");
        writer.println("        <thead>");
        writer.println("            <tr>");
        writer.println("                <th>Email</th>");
        writer.println("                <th>Company</th>");
        writer.println("                <th>Status</th>");
        writer.println("            </tr>");
        writer.println("        </thead>");
        writer.println("        <tbody>");

        if (leads.isEmpty()) {
            writer.println("            <tr>");
            writer.println("                <td colspan='3' style='text-align: center;'>Нет данных</td>");
            writer.println("            </tr>");
        } else {
            for (Lead lead : leads) {
                writer.println("            <tr>");
                writer.println("                <td>" + escapeHtml(lead.email()) + "</td>");
                writer.println("                <td>" + escapeHtml(lead.company()) + "</td>");
                writer.println("                <td>" + escapeHtml(lead.status().toString()) + "</td>");
                writer.println("            </tr>");
            }
        }

        writer.println("        </tbody>");
        writer.println("    </table>");
        writer.println("</body>");
        writer.println("</html>");
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}