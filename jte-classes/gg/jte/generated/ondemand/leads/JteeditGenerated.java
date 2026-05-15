package gg.jte.generated.ondemand.leads;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JteeditGenerated {
	public static final String JTE_NAME = "leads/edit.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,16,16,16,16,16,17,17,17,17,17,17,17,17,17,23,23,23,23,23,23,23,23,23,31,31,31,31,31,31,31,31,31,39,39,39,39,39,39,39,39,39,48,48,50,50,52,52,54,54,56,56,58,58,60,60,62,62,64,64,79,79,79,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Lead lead) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <title>Редактирование лида</title>\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-100\">\r\n<div class=\"container mx-auto p-6\">\r\n    <div class=\"max-w-md mx-auto bg-white rounded-2xl shadow-xl p-8\">\r\n        <h1 class=\"text-2xl font-bold mb-6 text-blue-700\">Редактирование лида</h1>\r\n\r\n        <form method=\"post\" action=\"/leads/");
		jteOutput.setContext("form", "action");
		jteOutput.writeUserContent(String.valueOf(lead.id()));
		jteOutput.setContext("form", null);
		jteOutput.writeContent("\" class=\"space-y-5\">\r\n            <input type=\"hidden\" name=\"id\"");
		var __jte_html_attribute_0 = String.valueOf(lead.id());
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n\r\n            <div>\r\n                <label for=\"name\" class=\"block text-sm font-medium text-gray-700 mb-2\">\r\n                    Name\r\n                </label>\r\n                <input type=\"text\" id=\"name\" name=\"name\"");
		var __jte_html_attribute_1 = lead.name();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required\r\n                       class=\"w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition\">\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"email\" class=\"block text-sm font-medium text-gray-700 mb-2\">\r\n                    Email\r\n                </label>\r\n                <input type=\"email\" id=\"email\" name=\"email\"");
		var __jte_html_attribute_2 = lead.email();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required\r\n                       class=\"w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition\">\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"company\" class=\"block text-sm font-medium text-gray-700 mb-2\">\r\n                    Company\r\n                </label>\r\n                <input type=\"text\" id=\"company\" name=\"company\"");
		var __jte_html_attribute_3 = lead.company();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_3);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required\r\n                       class=\"w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition\">\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"status\" class=\"block text-sm font-medium text-gray-700 mb-2\">\r\n                    Status\r\n                </label>\r\n                <select name=\"status\" id=\"status\" class=\"w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500\">\r\n                    ");
		if (lead.status().name() == "NEW") {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\" selected>Новый</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\">Новый</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n\r\n                    ");
		if (lead.status().name() == "CONTACTED") {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\" selected>Связались</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\">Связались</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n\r\n                    ");
		if (lead.status().name() == "QUALIFIED") {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\" selected>Квалифицированный</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\">Квалифицированный</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                </select>\r\n            </div>\r\n\r\n            <button type=\"submit\" class=\"w-full bg-blue-600 text-white px-4 py-2 rounded-xl hover:bg-blue-700 transition duration-200 font-medium\">\r\n                Сохранить изменения\r\n            </button>\r\n\r\n            <a href=\"/leads\" class=\"block text-center text-sm text-gray-500 hover:text-gray-700 transition mt-3\">\r\n                Отмена\r\n            </a>\r\n        </form>\r\n    </div>\r\n</div>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Lead lead = (Lead)params.get("lead");
		render(jteOutput, jteHtmlInterceptor, lead);
	}
}
