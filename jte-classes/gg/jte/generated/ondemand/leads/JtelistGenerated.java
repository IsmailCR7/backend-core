package gg.jte.generated.ondemand.leads;
import java.util.List;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,3,5,5,5,5,11,11,11,11,28,34,34,34,34,34,34,34,34,34,39,39,39,39,39,39,39,39,39,44,44,44,44,44,44,44,44,44,50,50,52,52,54,54,56,56,58,58,60,60,62,62,64,64,66,66,68,68,70,70,72,72,84,97,97,99,99,99,100,100,100,101,101,101,103,103,105,105,107,107,109,109,112,112,112,112,117,117,117,117,118,118,118,118,125,125,130,130,130,133,153,153,153,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Lead> leads, LeadStatus status, String name, String email, String company) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-lg p-6 border-t-4 border-pobeda-blue\">\r\n        <div class=\"flex justify-between items-center mb-6\">\r\n            <div>\r\n                <h2 class=\"text-2xl font-bold text-pobeda-blue\">Lead List</h2>\r\n                <p class=\"text-gray-500 text-sm mt-1\">Управление лидами</p>\r\n            </div>\r\n            <div>\r\n                <a\r\n                    href=\"/leads/new\"\r\n                    class=\"bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition duration-200 font-medium inline-block\"\r\n                >\r\n                    + Добавить лида\r\n                </a>\r\n            </div>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"mb-6\">\r\n            <form action=\"/leads\" method=\"get\" class=\"bg-pobeda-light p-4 rounded-lg\">\r\n                <div class=\"grid grid-cols-1 md:grid-cols-4 gap-4\">\r\n                    <div>\r\n                        <label class=\"block text-sm font-medium text-gray-700 mb-1\">Name</label>\r\n                        <input type=\"text\" name=\"name\"");
				var __jte_html_attribute_0 = name;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                               class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue\">\r\n                    </div>\r\n                    <div>\r\n                        <label class=\"block text-sm font-medium text-gray-700 mb-1\">Email</label>\r\n                        <input type=\"text\" name=\"email\"");
				var __jte_html_attribute_1 = email;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                               class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue\">\r\n                    </div>\r\n                    <div>\r\n                        <label class=\"block text-sm font-medium text-gray-700 mb-1\">Company</label>\r\n                        <input type=\"text\" name=\"company\"");
				var __jte_html_attribute_2 = company;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                               class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue\">\r\n                    </div>\r\n                    <div>\r\n                        <label class=\"block text-sm font-medium text-gray-700 mb-1\">Status</label>\r\n                        <select name=\"status\" class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue\">\r\n                            ");
				if (status == null) {
					jteOutput.writeContent("\r\n                                <option value=\"\" selected></option>\r\n                            ");
				} else {
					jteOutput.writeContent("\r\n                                <option value=\"\"></option>\r\n                            ");
				}
				jteOutput.writeContent("\r\n\r\n                            ");
				if (status == LeadStatus.NEW) {
					jteOutput.writeContent("\r\n                                <option value=\"NEW\" selected>Новый</option>\r\n                            ");
				} else {
					jteOutput.writeContent("\r\n                                <option value=\"NEW\">Новый</option>\r\n                            ");
				}
				jteOutput.writeContent("\r\n\r\n                            ");
				if (status == LeadStatus.CONTACTED) {
					jteOutput.writeContent("\r\n                                <option value=\"CONTACTED\" selected>Связались</option>\r\n                            ");
				} else {
					jteOutput.writeContent("\r\n                                <option value=\"CONTACTED\">Связались</option>\r\n                            ");
				}
				jteOutput.writeContent("\r\n\r\n                            ");
				if (status == LeadStatus.QUALIFIED) {
					jteOutput.writeContent("\r\n                                <option value=\"QUALIFIED\" selected>Квалифицированный</option>\r\n                            ");
				} else {
					jteOutput.writeContent("\r\n                                <option value=\"QUALIFIED\">Квалифицированный</option>\r\n                            ");
				}
				jteOutput.writeContent("\r\n                        </select>\r\n                    </div>\r\n                </div>\r\n                <div class=\"mt-4 text-right\">\r\n                    <button type=\"submit\" class=\"bg-pink-500 text-white px-6 py-2 rounded-md hover:bg-pink-600 transition duration-200 font-medium\">\r\n                        Найти\r\n                    </button>\r\n                </div>\r\n            </form>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"overflow-x-auto\">\r\n            <table class=\"min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden\">\r\n                <thead class=\"bg-pobeda-blue text-white\">\r\n                    <tr>\r\n                        <th class=\"px-4 py-3 text-left\">Name</th>\r\n                        <th class=\"px-4 py-3 text-left\">Email</th>\r\n                        <th class=\"px-4 py-3 text-left\">Company</th>\r\n                        <th class=\"px-4 py-3 text-left\">Status</th>\r\n                        <th class=\"px-4 py-3 text-left\" colspan=\"2\">Действия</th>\r\n                    </tr>\r\n                </thead>\r\n                <tbody>\r\n                ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                    <tr class=\"border-t border-gray-200 hover:bg-pobeda-light transition\">\r\n                        <td class=\"px-4 py-3 font-medium\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.name());
					jteOutput.writeContent("</td>\r\n                        <td class=\"px-4 py-3\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                        <td class=\"px-4 py-3\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company());
					jteOutput.writeContent("</td>\r\n                        <td class=\"px-4 py-3\">\r\n                            ");
					if (lead.status() == LeadStatus.NEW) {
						jteOutput.writeContent("\r\n                                <span class=\"px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800\">Новый</span>\r\n                            ");
					} else if (lead.status() == LeadStatus.CONTACTED) {
						jteOutput.writeContent("\r\n                                <span class=\"px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800\">Связались</span>\r\n                            ");
					} else {
						jteOutput.writeContent("\r\n                                <span class=\"px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800\">Квалифицированный</span>\r\n                            ");
					}
					jteOutput.writeContent("\r\n                        </td>\r\n                        <td class=\"px-4 py-3\">\r\n                            <a href=\"/leads/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(String.valueOf(lead.id()));
					jteOutput.setContext("a", null);
					jteOutput.writeContent("/edit\" class=\"text-pobeda-blue hover:underline font-medium\">\r\n                                Редактировать\r\n                            </a>\r\n                        </td>\r\n                        <td class=\"px-4 py-3\">\r\n                            <form action=\"/leads/");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(lead.id().toString());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("/delete\" method=\"post\" class=\"inline\"\r\n                                  onclick=\"return confirm('Удалить лида c e-mail: ");
					jteOutput.setContext("form", "onclick");
					jteOutput.writeUserContent(lead.email());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("?')\">\r\n                                <button type=\"submit\" class=\"bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 transition duration-200 text-sm\">\r\n                                    Удалить\r\n                                </button>\r\n                            </form>\r\n                        </td>\r\n                    </tr>\r\n                ");
				}
				jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        </div>\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent("\r\n\r\n<style>\r\n    ");
		jteOutput.writeContent("\r\n    .bg-pobeda-blue {\r\n        background-color: #0066CC;\r\n    }\r\n    .bg-pobeda-light {\r\n        background-color: #E8F2FF;\r\n    }\r\n    .text-pobeda-blue {\r\n        color: #0066CC;\r\n    }\r\n    .border-pobeda-blue {\r\n        border-color: #0066CC;\r\n    }\r\n    .hover\\:bg-pobeda-dark:hover {\r\n        background-color: #0052A3;\r\n    }\r\n    .focus\\:ring-pobeda-blue:focus {\r\n        --tw-ring-color: #0066CC;\r\n    }\r\n</style>\r\n`)\"");
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
