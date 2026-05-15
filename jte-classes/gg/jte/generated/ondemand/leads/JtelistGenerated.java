package gg.jte.generated.ondemand.leads;
import java.util.List;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,27,27,32,32,32,32,32,32,32,32,32,36,36,36,36,36,36,36,36,36,40,40,40,40,40,40,40,40,40,57,70,70,72,72,72,73,73,73,74,74,74,76,76,78,78,80,80,82,82,85,85,85,85,86,86,86,86,87,87,87,87,94,94,101,101,101,4,5,6,7,8,8,8,8};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Lead> leads, LeadStatus status, String name, String email, String company) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <title>Lead List</title>\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-100\">\r\n<div class=\"container mx-auto p-6\">\r\n    <div class=\"bg-white rounded-2xl shadow-xl p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4 text-blue-700\">Lead List</h2>\r\n\r\n        <div class=\"mb-4\">\r\n            <a href=\"/leads/new\" class=\"bg-green-500 text-white px-3 py-2 rounded-lg hover:bg-green-600 transition duration-200 inline-block\">\r\n                + Добавить лида\r\n            </a>\r\n        </div>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <form action=\"/leads\" method=\"get\" class=\"mb-4 p-4 rounded-xl bg-blue-50\">\r\n            <div class=\"flex flex-wrap gap-3 items-end\">\r\n                <label class=\"text-sm font-medium text-gray-700\">\r\n                    Name:\r\n                    <input type=\"text\" name=\"name\"");
		var __jte_html_attribute_0 = name != null ? name : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" class=\"rounded-lg border-gray-300\">\r\n                </label>\r\n                <label class=\"text-sm font-medium text-gray-700\">\r\n                    Email:\r\n                    <input type=\"text\" name=\"email\"");
		var __jte_html_attribute_1 = email != null ? email : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" class=\"rounded-lg border-gray-300\">\r\n                </label>\r\n                <label class=\"text-sm font-medium text-gray-700\">\r\n                    Company:\r\n                    <input type=\"text\" name=\"company\"");
		var __jte_html_attribute_2 = company != null ? company : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" class=\"rounded-lg border-gray-300\">\r\n                </label>\r\n                <label class=\"text-sm font-medium text-gray-700\">\r\n                    Status:\r\n                    <select name=\"status\" class=\"rounded-lg border-gray-300\">\r\n                        <option value=\"\">Все статусы</option>\r\n                        <option value=\"NEW\">Новый</option>\r\n                        <option value=\"CONTACTED\">Связались</option>\r\n                        <option value=\"QUALIFIED\">Квалифицированный</option>\r\n                    </select>\r\n                </label>\r\n                <button type=\"submit\" class=\"bg-blue-600 text-white rounded-lg px-4 py-1.5 hover:bg-blue-700 transition duration-200\">\r\n                    Поиск\r\n                </button>\r\n            </div>\r\n        </form>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div class=\"overflow-x-auto rounded-xl border border-gray-200\">\r\n            <table class=\"min-w-full bg-white\">\r\n                <thead class=\"bg-blue-700 text-white\">\r\n                    <tr>\r\n                        <th class=\"px-4 py-3 text-left rounded-tl-xl\">Name</th>\r\n                        <th class=\"px-4 py-3 text-left\">Email</th>\r\n                        <th class=\"px-4 py-3 text-left\">Company</th>\r\n                        <th class=\"px-4 py-3 text-left\">Status</th>\r\n                        <th class=\"px-4 py-3 text-left rounded-tr-xl\">Actions</th>\r\n                    </tr>\r\n                </thead>\r\n                <tbody>\r\n                    ");
		for (var lead : leads) {
			jteOutput.writeContent("\r\n                        <tr class=\"border-t border-gray-100 hover:bg-gray-50 transition duration-150\">\r\n                            <td class=\"px-4 py-3\">");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(lead.name() != null ? lead.name() : "");
			jteOutput.writeContent("</td>\r\n                            <td class=\"px-4 py-3\">");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(lead.email());
			jteOutput.writeContent("</td>\r\n                            <td class=\"px-4 py-3\">");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(lead.company());
			jteOutput.writeContent("</td>\r\n                            <td class=\"px-4 py-3\">\r\n                                ");
			if (lead.status().name() == "NEW") {
				jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium\">Новый</span>\r\n                                ");
			} else if (lead.status().name() == "CONTACTED") {
				jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full text-xs font-medium\">Связались</span>\r\n                                ");
			} else {
				jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 bg-green-100 text-green-700 rounded-full text-xs font-medium\">Квалифицированный</span>\r\n                                ");
			}
			jteOutput.writeContent("\r\n                            </td>\r\n                            <td class=\"px-4 py-3\">\r\n                                <a href=\"/leads/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(String.valueOf(lead.id()));
			jteOutput.setContext("a", null);
			jteOutput.writeContent("/edit\" class=\"text-blue-600 hover:text-blue-800 font-medium transition\">Редактировать</a>\r\n                                <form action=\"/leads/");
			jteOutput.setContext("form", "action");
			jteOutput.writeUserContent(String.valueOf(lead.id()));
			jteOutput.setContext("form", null);
			jteOutput.writeContent("/delete\" method=\"post\" class=\"inline ml-2\"\r\n                                      onsubmit=\"return confirm('Удалить лида ");
			jteOutput.setContext("form", "onsubmit");
			jteOutput.writeUserContent(lead.email());
			jteOutput.setContext("form", null);
			jteOutput.writeContent("?')\">\r\n                                    <button type=\"submit\" class=\"bg-red-500 text-white px-3 py-1 rounded-lg hover:bg-red-600 transition duration-200 text-sm\">\r\n                                        Удалить\r\n                                    </button>\r\n                                </form>\r\n                            </td>\r\n                        </tr>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        </div>\r\n    </div>\r\n</div>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Lead> leads = (List<Lead>)params.get("leads");
		LeadStatus status = (LeadStatus)params.get("status");
		String name = (String)params.get("name");
		String email = (String)params.get("email");
		String company = (String)params.get("company");
		render(jteOutput, jteHtmlInterceptor, leads, status, name, email, company);
	}
}
