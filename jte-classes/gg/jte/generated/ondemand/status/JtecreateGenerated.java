package gg.jte.generated.ondemand.status;
import ru.mentee.power.crm.model.Status;
@SuppressWarnings("unchecked")
public final class JtecreateGenerated {
	public static final String JTE_NAME = "status/create.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,4,4,6,6,11,25,32,41,41,41,41,41,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Status status) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"max-w-md mx-auto mt-8\">\r\n        <h1 class=\"text-2xl font-bold mb-6\">Добавить нового статуса</h1>\r\n\r\n        <form action=\"/statuses\" method=\"post\" class=\"space-y-4\">\r\n            ");
				jteOutput.writeContent("\r\n            <div>\r\n                <label for=\"email\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                    Status\r\n                </label>\r\n                <input\r\n                        type=\"text\"\r\n                        id=\"status\"\r\n                        name=\"status\"\r\n                        required\r\n                        class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                />\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <button\r\n                    type=\"submit\"\r\n                    class=\"w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500\"\r\n            >\r\n                Создать статус\r\n            </button>\r\n            ");
				jteOutput.writeContent("\r\n            <a\r\n                    href=\"/statuses\"\r\n                    class=\"block text-center text-sm text-gray-600 hover:text-gray-900\"\r\n            >\r\n                Отмена\r\n            </a>\r\n        </form>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Status status = (Status)params.get("status");
		render(jteOutput, jteHtmlInterceptor, status);
	}
}
