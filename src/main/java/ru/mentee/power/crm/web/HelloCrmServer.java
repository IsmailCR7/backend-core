package ru.mentee.power.crm.web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HelloCrmServer {

    private final HttpServer server;
    private final int port;

    public HelloCrmServer(int port) throws IOException {
        this.port = port;

        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("✅ Сервер создан на порту " + port);
    }

    public void start() {
        server.createContext("/hello", new HelloHandler());


        server.start();
        System.out.println("🚀 Server started on http://localhost:" + port + "/hello");
        System.out.println("Нажмите Ctrl+C для остановки сервера");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("🛑 Сервер остановлен");
        }
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("📨 Получен " + method + " запрос на путь: " + path);

            try {
                if ("GET".equals(method)) {
                    String htmlResponse = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "    <meta charset='UTF-8'>\n" +
                            "    <title>CRM System</title>\n" +
                            "    <style>\n" +
                            "        body { font-family: Arial, sans-serif; margin: 40px; }\n" +
                            "        h1 { color: #2c3e50; }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <h1>👋 Hello CRM!</h1>\n" +
                            "    <p>Сервер успешно работает!</p>\n" +
                            "    <p>Текущее время: " + new java.util.Date() + "</p>\n" +
                            "</body>\n" +
                            "</html>";


                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    byte[] responseBytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseBytes);
                    os.close();

                    System.out.println("Ответ отправлен, размер: " + responseBytes.length + " байт");
                } else {
                    exchange.sendResponseHeaders(405, -1);
                    System.out.println("Метод " + method + " не поддерживается");
                }
            } finally {
                exchange.close();
            }
        }
    }
}