package gg.jte.generated.ondemand.layout;
@SuppressWarnings("unchecked")
public final class JtemainGenerated {
	public static final String JTE_NAME = "layout/main.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,8,8,11,16,32,34,34,34,37,46,46,46,0,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, gg.jte.Content content) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>EagleCRM - Lead Management by Ismail</title>\r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"/favicon.png\">\r\n    <link rel=\"icon\" type=\"image/x-icon\" href=\"/favicon.png\">\r\n    ");
		jteOutput.writeContent("\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-100 min-h-screen\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <nav class=\"bg-white shadow-lg\">\r\n        <div class=\"max-w-7xl mx-auto px-4\">\r\n            <div class=\"flex justify-between h-16\">\r\n                <div class=\"flex items-center\">\r\n                    <h1 class=\"text-xl font-bold text-gray-800\">EagleCRM System</h1>\r\n                </div>\r\n                <div class=\"flex items-center space-x-4\">\r\n                    <a href=\"/leads\" class=\"text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium\">\r\n                        Лиды\r\n                    </a>\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </nav>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <main class=\"max-w-7xl mx-auto py-6 sm:px-6 lg:px-8\">\r\n        ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n    </main>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <footer class=\"bg-white shadow-lg mt-8\">\r\n        <div class=\"max-w-7xl mx-auto py-4 px-4 text-center text-gray-500 text-sm\">\r\n            © 2026 CRM Project. Все права защищены.\r\n        </div>\r\n    </footer>\r\n\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		gg.jte.Content content = (gg.jte.Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, content);
	}
}
