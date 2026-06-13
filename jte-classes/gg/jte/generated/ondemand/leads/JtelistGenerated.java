package gg.jte.generated.ondemand.leads;
import java.util.List;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,3,5,5,5,5,11,11,11,11,29,29,29,29,29,29,29,29,29,33,33,33,33,33,33,33,33,33,37,37,37,37,37,37,37,37,37,42,42,44,44,46,46,48,48,50,50,52,52,54,54,56,56,58,58,60,60,62,62,64,64,86,86,88,88,88,89,89,89,91,91,91,95,95,95,100,100,100,100,107,107,107,107,109,109,109,109,120,120,124,124,124,124,124,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Lead> leads, LeadStatus status, String name, String email, String companyName) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Lead List</h2>\r\n\r\n        <div>\r\n            <a\r\n                    href=\"/leads/new\"\r\n                    class=\"bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600\"\r\n            >\r\n                + Добавить лида\r\n            </a>\r\n        </div>\r\n        <br>\r\n\r\n        <div class=\"mb-4 flex gap-2 justify-between items-center\">\r\n            <form action=\"/leads\" method=\"get\" class=\"px-4 py-2 rounded bg-blue-500\">\r\n                <label for=\"name\">\r\n                    Name:\r\n                    <input type=\"text\" name=\"name\"");
				var __jte_html_attribute_0 = name;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n                </label>\r\n                <label for=\"email\">\r\n                    Email:\r\n                    <input type=\"text\" name=\"email\"");
				var __jte_html_attribute_1 = email;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n                </label>\r\n                <label for=\"company\">\r\n                    Company:\r\n                    <input type=\"text\" name=\"companyName\"");
				var __jte_html_attribute_2 = companyName;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n                </label>\r\n                <label for=\"status\">\r\n                    Status\r\n                    <select name=\"status\">\r\n                        ");
				if (status == null) {
					jteOutput.writeContent("\r\n                            <option value=\"\" selected></option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"\"></option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n\r\n                        ");
				if (status == LeadStatus.NEW) {
					jteOutput.writeContent("\r\n                            <option value=\"NEW\" selected>Новый</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"NEW\">Новый</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n\r\n                        ");
				if (status == LeadStatus.CONTACTED) {
					jteOutput.writeContent("\r\n                            <option value=\"CONTACTED\" selected>Связались</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"CONTACTED\">Связались</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n\r\n                        ");
				if (status == LeadStatus.QUALIFIED) {
					jteOutput.writeContent("\r\n                            <option value=\"QUALIFIED\" selected>Квалифицированный</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"QUALIFIED\">Квалифицированный</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                    </select>\r\n                </label>\r\n                <button type=\"submit\"\r\n                        class=\"bg-white rounded px-1 py-1 hover:bg-gray-200\"\r\n                >\r\n                    Поиск\r\n                </button>\r\n            </form>\r\n        </div>\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n            <tr>\r\n                <th class=\"px-4 py-2 text-left\">Name</th>\r\n                <th class=\"px-4 py-2 text-left\">Email</th>\r\n                <th class=\"px-4 py-2 text-left\">Company</th>\r\n                <th class=\"px-4 py-2 text-left\">Status</th>\r\n                <th class=\"px-4 py-2 text-left\"></th>\r\n                <th class=\"px-4 py-2 text-left\"></th>\r\n            </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                <tr class=\"border-t hover:bg-gray-50\">\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.name());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">\r\n                        ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company() != null ? lead.company().getName() : "не указана");
					jteOutput.writeContent("\r\n                    </td>\r\n                    <td class=\"px-4 py-2\">\r\n                            <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                                ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(lead.status());
					jteOutput.writeContent("\r\n                            </span>\r\n                    </td>\r\n                    <td>\r\n                        <a\r\n                                href=\"/leads/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(String.valueOf(lead.id()));
					jteOutput.setContext("a", null);
					jteOutput.writeContent("/edit\"\r\n                                class=\"text-blue-600 hover:underline\"\r\n                        >\r\n                            Редактировать\r\n                        </a>\r\n                    </td>\r\n                    <td>\r\n                        <form action=\"/leads/");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(lead.id().toString());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("/delete\"\r\n                              method=\"post\" class=\"inline\"\r\n                              onclick=\"return confirm('Удалить лида c e-mail: ");
					jteOutput.setContext("form", "onclick");
					jteOutput.writeUserContent(lead.email());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("?')\"\r\n                        >\r\n                            <button\r\n                                    type=\"submit\"\r\n                                    class=\"bg-red-600 text-white px-2 rounded hover:bg-red-700\"\r\n                            >\r\n                                Удалить\r\n                            </button>\r\n                        </form>\r\n                    </td>\r\n                </tr>\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Lead> leads = (List<Lead>)params.get("leads");
		LeadStatus status = (LeadStatus)params.get("status");
		String name = (String)params.get("name");
		String email = (String)params.get("email");
		String companyName = (String)params.get("companyName");
		render(jteOutput, jteHtmlInterceptor, leads, status, name, email, companyName);
	}
}
