package gg.jte.generated.ondemand.leads;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,4,4,4,4,6,10,20,23,30,30,30,30,30,30,35,42,42,44,44,46,46,47,47,49,49,51,51,52,52,54,54,56,56,57,57,59,59,61,61,65,76,78,82,83,83,87,87,91,91,93,94,94,98,98,102,102,104,105,105,109,109,113,113,115,116,116,120,120,124,124,127,128,128,132,132,132,134,134,137,137,141,141,152,152,154,154,154,155,155,155,157,157,159,159,159,161,161,163,163,163,165,165,167,167,167,169,169,172,172,172,172,176,176,176,176,178,178,178,178,186,186,189,189,191,191,191,191,191,191,0,1,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<ru.mentee.power.crm.model.Lead> leads, ru.mentee.power.crm.model.LeadStatus currentFilter, java.lang.String search) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"flex justify-between items-center mb-4\">\r\n            <h2 class=\"text-2xl font-bold\">Список лидов</h2>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <a href=\"/leads/new\"\r\n               class=\"bg-green-500 hover:bg-green-600 text-white font-medium px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2\">\r\n                <svg xmlns=\"http://www.w3.org/2000/svg\" class=\"h-5 w-5\" viewBox=\"0 0 20 20\" fill=\"currentColor\">\r\n                    <path fill-rule=\"evenodd\" d=\"M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z\" clip-rule=\"evenodd\" />\r\n                </svg>\r\n                Добавить лида\r\n            </a>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"bg-white shadow rounded-lg p-4 mb-6\">\r\n            <form method=\"get\" action=\"/leads\" class=\"flex gap-4 items-end\">\r\n                ");
				jteOutput.writeContent("\r\n                <div class=\"flex-1\">\r\n                    <label class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                        Поиск по имени или email\r\n                    </label>\r\n                    <input type=\"text\"\r\n                           name=\"search\"\r\n                           value=\"");
				if (search != null) {
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(search);
					jteOutput.setContext("input", null);
				}
				jteOutput.writeContent("\"\r\n                           placeholder=\"Введите имя или email...\"\r\n                           class=\"w-full px-3 py-2 border border-gray-300 rounded-md\">\r\n                </div>\r\n\r\n                ");
				jteOutput.writeContent("\r\n                <div class=\"w-48\">\r\n                    <label class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                        Статус\r\n                    </label>\r\n                    <select name=\"status\" class=\"w-full px-3 py-2 border border-gray-300 rounded-md\">\r\n                        <option value=\"\">Все статусы</option>\r\n                        ");
				if (currentFilter != null && currentFilter.toString() == "NEW") {
					jteOutput.writeContent("\r\n                            <option value=\"NEW\" selected>Новый</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"NEW\">Новый</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                        ");
				if (currentFilter != null && currentFilter.toString() == "CONTACTED") {
					jteOutput.writeContent("\r\n                            <option value=\"CONTACTED\" selected>В контакте</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"CONTACTED\">В контакте</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                        ");
				if (currentFilter != null && currentFilter.toString() == "QUALIFIED") {
					jteOutput.writeContent("\r\n                            <option value=\"QUALIFIED\" selected>Квалифицирован</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"QUALIFIED\">Квалифицирован</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                        ");
				if (currentFilter != null && currentFilter.toString() == "LOST") {
					jteOutput.writeContent("\r\n                            <option value=\"LOST\" selected>Потерян</option>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                            <option value=\"LOST\">Потерян</option>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                    </select>\r\n                </div>\r\n\r\n                ");
				jteOutput.writeContent("\r\n                <div class=\"flex gap-2\">\r\n                    <button type=\"submit\" class=\"px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600\">\r\n                        🔍 Найти\r\n                    </button>\r\n                    <a href=\"/leads\" class=\"px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600\">\r\n                        ✖️ Сбросить\r\n                    </a>\r\n                </div>\r\n            </form>\r\n        </div>\r\n        ");
				jteOutput.writeContent("\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"mb-6\">\r\n            <h3 class=\"text-lg font-semibold mb-2\">Фильтр по статусу:</h3>\r\n            <div class=\"flex flex-wrap gap-2\">\r\n                ");
				jteOutput.writeContent("\r\n                ");
				if (currentFilter == null) {
					jteOutput.writeContent("\r\n                    <a href=\"/leads\" class=\"px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white\">\r\n                        Все\r\n                    </a>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <a href=\"/leads\" class=\"px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300\">\r\n                        Все\r\n                    </a>\r\n                ");
				}
				jteOutput.writeContent("\r\n\r\n                ");
				jteOutput.writeContent("\r\n                ");
				if (currentFilter != null && currentFilter.toString() == "NEW") {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=NEW\" class=\"px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white\">\r\n                        Новые (NEW)\r\n                    </a>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=NEW\" class=\"px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300\">\r\n                        Новые (NEW)\r\n                    </a>\r\n                ");
				}
				jteOutput.writeContent("\r\n\r\n                ");
				jteOutput.writeContent("\r\n                ");
				if (currentFilter != null && currentFilter.toString() == "CONTACTED") {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=CONTACTED\" class=\"px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white\">\r\n                        В работе (CONTACTED)\r\n                    </a>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=CONTACTED\" class=\"px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300\">\r\n                        В работе (CONTACTED)\r\n                    </a>\r\n                ");
				}
				jteOutput.writeContent("\r\n\r\n                ");
				jteOutput.writeContent("\r\n                ");
				if (currentFilter != null && currentFilter.toString() == "QUALIFIED") {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=QUALIFIED\" class=\"px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white\">\r\n                        Квалифицированные (QUALIFIED)\r\n                    </a>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <a href=\"/leads?status=QUALIFIED\" class=\"px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300\">\r\n                        Квалифицированные (QUALIFIED)\r\n                    </a>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </div>\r\n\r\n            ");
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
					jteOutput.writeContent("\r\n            <table class=\"min-w-full bg-white border border-gray-200\">\r\n                <thead class=\"bg-gray-100\">\r\n                    <tr>\r\n                        <th class=\"px-4 py-2 text-left\">Email</th>\r\n                        <th class=\"px-4 py-2 text-left\">Company</th>\r\n                        <th class=\"px-4 py-2 text-left\">Status</th>\r\n                        <th class=\"px-4 py-2 text-left\">Действия</th>\r\n                    </tr>\r\n                </thead>\r\n                <tbody>\r\n                    ");
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
						jteOutput.writeContent("\r\n                            </td>\r\n                            <td class=\"px-4 py-2\">\r\n                                <a href=\"/leads/");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(String.valueOf(lead.id()));
						jteOutput.setContext("a", null);
						jteOutput.writeContent("/edit\"\r\n                                   class=\"bg-pink-100 hover:bg-pink-200 text-pink-700 px-3 py-1 rounded border border-pink-300\">\r\n                                    Редактировать\r\n                                </a>\r\n                                <form method=\"post\" action=\"/leads/");
						jteOutput.setContext("form", "action");
						jteOutput.writeUserContent(String.valueOf(lead.id()));
						jteOutput.setContext("form", null);
						jteOutput.writeContent("/delete\"\r\n                                     class=\"inline\"\r\n                                     onsubmit=\"return confirm('Удалить лида ");
						jteOutput.setContext("form", "onsubmit");
						jteOutput.writeUserContent(lead.company());
						jteOutput.setContext("form", null);
						jteOutput.writeContent("?')\">\r\n                                  <button type=\"submit\"\r\n                                         class=\"bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700\">\r\n                                     Удалить\r\n                                  </button>\r\n                                </form>\r\n                            </td>\r\n                        </tr>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        ");
				}
				jteOutput.writeContent("\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent(">");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<ru.mentee.power.crm.model.Lead> leads = (java.util.List<ru.mentee.power.crm.model.Lead>)params.get("leads");
		ru.mentee.power.crm.model.LeadStatus currentFilter = (ru.mentee.power.crm.model.LeadStatus)params.get("currentFilter");
		java.lang.String search = (java.lang.String)params.get("search");
		render(jteOutput, jteHtmlInterceptor, leads, currentFilter, search);
	}
}
