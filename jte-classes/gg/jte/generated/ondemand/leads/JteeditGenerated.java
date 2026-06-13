package gg.jte.generated.ondemand.leads;
import java.util.List;
import ru.mentee.power.crm.model.Company;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JteeditGenerated {
	public static final String JTE_NAME = "leads/edit.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,18,18,18,18,18,19,21,21,21,21,21,21,21,21,21,30,30,30,30,30,30,30,30,30,44,44,44,44,44,44,44,44,44,56,56,57,57,58,58,58,58,58,58,58,58,58,58,58,58,59,59,60,60,60,60,60,60,60,60,60,60,60,60,61,61,62,62,70,70,72,72,74,74,76,76,78,78,80,80,82,82,84,84,86,86,105,105,105,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Company> companies, Lead lead) {
		jteOutput.writeContent("\r\n\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <title>Редактирование лида</title>\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-100\">\r\n<div class=\"container mx-auto p-6\">\r\n    <h1 class=\"text-2xl font-bold mb-4\">Редактирование лида</h1>\r\n    <form method=\"post\" action=\"/leads/");
		jteOutput.setContext("form", "action");
		jteOutput.writeUserContent(String.valueOf(lead.id()));
		jteOutput.setContext("form", null);
		jteOutput.writeContent("\" class=\"bg-white p-6 rounded shadow\">\r\n        ");
		jteOutput.writeContent("\r\n        <div>\r\n            <input type=\"hidden\" name=\"id\"");
		var __jte_html_attribute_0 = String.valueOf(lead.id());
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n        </div>\r\n        <div>\r\n            <label for=\"name\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                Company\r\n            </label>\r\n            <input\r\n                    type=\"text\"\r\n                    id=\"name\"\r\n                   ");
		var __jte_html_attribute_1 = lead.name();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                    name=\"name\"\r\n                    required\r\n                    class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                    placeholder=\"Ivan\"\r\n            />\r\n        </div>\r\n        <div>\r\n            <label for=\"email\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                Email\r\n            </label>\r\n            <input\r\n                    type=\"email\"\r\n                    id=\"email\"\r\n                   ");
		var __jte_html_attribute_2 = lead.email();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                    name=\"email\"\r\n                    required\r\n                    class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                    placeholder=\"example@company.com\"\r\n            />\r\n        </div>\r\n        <div>\r\n            <label for=\"company\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                Company\r\n                <select name=\"companyId\">\r\n                    <option value=\"\">— Без компании —</option>\r\n                    ");
		for (Company company : companies) {
			jteOutput.writeContent("\r\n                        ");
			if (lead.getCompany() != null && lead.getCompany().getId().equals(company.getId())) {
				jteOutput.writeContent("\r\n                            <option");
				var __jte_html_attribute_3 = company.getId().toString();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(__jte_html_attribute_3);
					jteOutput.setContext("option", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" selected>");
				jteOutput.setContext("option", null);
				jteOutput.writeUserContent(company.getName());
				jteOutput.writeContent("</option>\r\n                        ");
			} else {
				jteOutput.writeContent("\r\n                            <option");
				var __jte_html_attribute_4 = company.getId().toString();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(__jte_html_attribute_4);
					jteOutput.setContext("option", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">");
				jteOutput.setContext("option", null);
				jteOutput.writeUserContent(company.getName());
				jteOutput.writeContent("</option>\r\n                        ");
			}
			jteOutput.writeContent("\r\n                    ");
		}
		jteOutput.writeContent("\r\n                </select>\r\n            </label>\r\n        </div>\r\n        <div>\r\n            <label for=\"status\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                Status\r\n                <select name = \"status\">\r\n                    ");
		if (lead.status() == LeadStatus.NEW) {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\" selected>Новый</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\">Новый</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n\r\n                    ");
		if (lead.status() == LeadStatus.CONTACTED) {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\" selected>Связались</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\">Связались</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n\r\n                    ");
		if (lead.status() == LeadStatus.QUALIFIED) {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\" selected>Квалифицированный</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\">Квалифицированный</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                </select>\r\n            </label>\r\n        </div>\r\n        <button\r\n                type=\"submit\"\r\n                class=\"w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500\"\r\n        >\r\n            Редактировать лида\r\n        </button>\r\n        <a\r\n                href=\"/leads\"\r\n                class=\"block text-center text-sm text-gray-600 hover:text-gray-900\"\r\n        >\r\n            Отмена\r\n        </a>\r\n    </form>\r\n</div>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Company> companies = (List<Company>)params.get("companies");
		Lead lead = (Lead)params.get("lead");
		render(jteOutput, jteHtmlInterceptor, companies, lead);
	}
}
