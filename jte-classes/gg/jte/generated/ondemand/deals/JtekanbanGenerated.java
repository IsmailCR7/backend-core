package gg.jte.generated.ondemand.deals;
import ru.mentee.power.crm.model.Deal;
import ru.mentee.power.crm.model.DealStatus;
@SuppressWarnings("unchecked")
public final class JtekanbanGenerated {
	public static final String JTE_NAME = "deals/kanban.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,15,15,15,17,17,17,19,19,21,21,21,22,22,22,24,25,25,25,25,27,27,27,27,27,27,27,27,27,41,50,50,52,52,55,55,55,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<DealStatus, java.util.List<Deal>> dealsByStatus) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <title>Воронка продаж</title>\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-100 p-8\">\r\n<h1 class=\"text-3xl font-bold mb-6\">Воронка продаж (Kanban)</h1>\r\n\r\n<div class=\"grid grid-cols-6 gap-4\">\r\n    ");
		for (DealStatus status : DealStatus.values()) {
			jteOutput.writeContent("\r\n        <div class=\"bg-white rounded shadow-md p-4\">\r\n            <h2 class=\"font-bold mb-4 text-center\">");
			jteOutput.setContext("h2", null);
			jteOutput.writeUserContent(status);
			jteOutput.writeContent("</h2>\r\n\r\n            ");
			for (Deal deal : dealsByStatus.getOrDefault(status, java.util.List.of())) {
				jteOutput.writeContent("\r\n                <div class=\"bg-gray-50 p-3 mb-2 rounded border\">\r\n                    <p class=\"font-semibold\">");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(deal.getAmount());
				jteOutput.writeContent(" ₽</p>\r\n                    <p class=\"text-sm text-gray-600\">Lead: ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(deal.getLeadId().toString());
				jteOutput.writeContent("</p>\r\n\r\n                    ");
				jteOutput.writeContent("\r\n                    <form action=\"/deals/");
				jteOutput.setContext("form", "action");
				jteOutput.writeUserContent(deal.getId().toString());
				jteOutput.setContext("form", null);
				jteOutput.writeContent("/transition\" method=\"post\" class=\"space-y-4\">\r\n                        <div>\r\n                            <input type=\"hidden\" name=\"leadId\"");
				var __jte_html_attribute_0 = String.valueOf(deal.getId());
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n                        </div>\r\n                        <div>\r\n                            <label for=\"newStatus\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                                Сменить статус на:\r\n                            </label>\r\n                            <select name=\"newStatus\" class=\"w-full\">\r\n                                <option value=\"QUALIFIED\">QUALIFIED</option>\r\n                                <option value=\"PROPOSAL_SENT\">PROPOSAL_SENT</option>\r\n                                <option value=\"NEGOTIATION\">NEGOTIATION</option>\r\n                                <option value=\"WON\">WON</option>\r\n                                <option value=\"LOST\">LOST</option>\r\n                            </select>\r\n                        </div>\r\n                        ");
				jteOutput.writeContent("\r\n                        <button\r\n                                type=\"submit\"\r\n                                class=\"w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500\"\r\n                        >\r\n                            Ок\r\n                        </button>\r\n                    </form>\r\n                </div>\r\n            ");
			}
			jteOutput.writeContent("\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n</div>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.Map<DealStatus, java.util.List<Deal>> dealsByStatus = (java.util.Map<DealStatus, java.util.List<Deal>>)params.get("dealsByStatus");
		render(jteOutput, jteHtmlInterceptor, dealsByStatus);
	}
}
