package gg.jte.generated.ondemand.status;
import java.util.List;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.model.Status;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "status/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,11,11,11,11,31,31,33,33,33,35,35,39,39,39,39,39,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Lead> leads, LeadStatus status, String email, String company, List<Status> statuses) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Status List</h2>\r\n\r\n        <div>\r\n            <a\r\n                    href=\"/statuses/new\"\r\n                    class=\"bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600\"\r\n            >\r\n                + Добавить статус\r\n            </a>\r\n        </div>\r\n        <br>\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n                <tr>\r\n                    <th class=\"px-4 py-2 text-left\">Status</th>\r\n                </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				for (var statusItem : statuses) {
					jteOutput.writeContent("\r\n                <tr class=\"border-t hover:bg-gray-50\">\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(statusItem.name());
					jteOutput.writeContent("</td>\r\n                </tr>\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Lead> leads = (List<Lead>)params.get("leads");
		LeadStatus status = (LeadStatus)params.get("status");
		String email = (String)params.get("email");
		String company = (String)params.get("company");
		List<Status> statuses = (List<Status>)params.get("statuses");
		render(jteOutput, jteHtmlInterceptor, leads, status, email, company, statuses);
	}
}
