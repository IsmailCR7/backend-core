package gg.jte.generated.ondemand.leads;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,3,3,3,3,7,11,14,16,16,16,16,16,20,23,25,25,25,25,25,29,32,34,34,34,34,34,38,41,43,43,43,43,43,48,49,49,53,53,53,55,55,58,58,62,62,72,72,74,74,74,75,75,75,77,77,79,79,79,81,81,83,83,83,85,85,87,87,87,89,89,92,92,95,95,97,97,97,98,98,98,0,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<ru.mentee.power.crm.model.Lead> leads, ru.mentee.power.crm.model.LeadStatus currentFilter) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Список лидов</h2>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"mb-6\">\r\n            <h3 class=\"text-lg font-semibold mb-2\">Фильтр по статусу:</h3>\r\n            <div class=\"flex flex-wrap gap-2\">\r\n                ");
				jteOutput.writeContent("\r\n                <a href=\"/leads\"\r\n                   class=\"px-4 py-2 rounded transition-colors inline-block\r\n                          ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == null ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">\r\n                    Все\r\n                </a>\r\n\r\n                ");
				jteOutput.writeContent("\r\n                <a href=\"/leads?status=NEW\"\r\n                   class=\"px-4 py-2 rounded transition-colors inline-block\r\n                          ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter != null && currentFilter.toString() == "NEW" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">\r\n                    Новые (NEW)\r\n                </a>\r\n\r\n                ");
				jteOutput.writeContent("\r\n                <a href=\"/leads?status=CONTACTED\"\r\n                   class=\"px-4 py-2 rounded transition-colors inline-block\r\n                          ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter != null && currentFilter.toString() == "CONTACTED" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">\r\n                    В работе (CONTACTED)\r\n                </a>\r\n\r\n                ");
				jteOutput.writeContent("\r\n                <a href=\"/leads?status=QUALIFIED\"\r\n                   class=\"px-4 py-2 rounded transition-colors inline-block\r\n                          ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter != null && currentFilter.toString() == "QUALIFIED" ?
                            "bg-blue-600 text-white" :
                            "bg-gray-200 hover:bg-gray-300");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">\r\n                    Квалифицированные (QUALIFIED)\r\n                </a>\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            ");
				if (currentFilter != null) {
					jteOutput.writeContent("\r\n                <p class=\"mt-4 text-sm text-gray-600 bg-blue-50 border border-blue-200 rounded-lg p-3\">\r\n                    <span class=\"font-medium\">Активный фильтр:</span>\r\n                    показаны лиды со статусом\r\n                    <span class=\"font-semibold\">");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(currentFilter);
					jteOutput.writeContent("</span>\r\n                </p>\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        ");
				if (leads.isEmpty()) {
					jteOutput.writeContent("\r\n            <div class=\"bg-yellow-50 border border-yellow-200 rounded-lg p-4\">\r\n                <p class=\"text-yellow-600\">Нет данных</p>\r\n            </div>\r\n        ");
				} else {
					jteOutput.writeContent("\r\n            <table class=\"min-w-full bg-white border border-gray-200\">\r\n                <thead class=\"bg-gray-100\">\r\n                    <tr>\r\n                        <th class=\"px-4 py-2 text-left\">Email</th>\r\n                        <th class=\"px-4 py-2 text-left\">Company</th>\r\n                        <th class=\"px-4 py-2 text-left\">Status</th>\r\n                    </tr>\r\n                </thead>\r\n                <tbody>\r\n                    ");
					for (var lead : leads) {
						jteOutput.writeContent("\r\n                        <tr class=\"border-t hover:bg-gray-50\">\r\n                            <td class=\"px-4 py-2\">");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(lead.email());
						jteOutput.writeContent("</td>\r\n                            <td class=\"px-4 py-2\">");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(lead.company());
						jteOutput.writeContent("</td>\r\n                            <td class=\"px-4 py-2\">\r\n                                ");
						if (lead.status().toString() == "NEW") {
							jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                                        ");
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(lead.status());
							jteOutput.writeContent("\r\n                                    </span>\r\n                                ");
						} else if (lead.status().toString() == "CONTACTED") {
							jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 rounded text-sm bg-blue-100 text-blue-800\">\r\n                                        ");
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(lead.status());
							jteOutput.writeContent("\r\n                                    </span>\r\n                                ");
						} else {
							jteOutput.writeContent("\r\n                                    <span class=\"px-2 py-1 rounded text-sm bg-purple-100 text-purple-800\">\r\n                                        ");
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(lead.status());
							jteOutput.writeContent("\r\n                                    </span>\r\n                                ");
						}
						jteOutput.writeContent("\r\n                            </td>\r\n                        </tr>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        ");
				}
				jteOutput.writeContent("\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<ru.mentee.power.crm.model.Lead> leads = (java.util.List<ru.mentee.power.crm.model.Lead>)params.get("leads");
		ru.mentee.power.crm.model.LeadStatus currentFilter = (ru.mentee.power.crm.model.LeadStatus)params.get("currentFilter");
		render(jteOutput, jteHtmlInterceptor, leads, currentFilter);
	}
}
