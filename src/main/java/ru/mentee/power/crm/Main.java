package ru.mentee.power.crm;

import ru.mentee.power.crm.web.HelloCrmServer;

public class Main {
    static void main(String[] args) throws Exception {
        int port = 8080;
        System.out.println("Запуск HTTP сервера");
        System.out.println("Порт: " + port);

        HelloCrmServer server = new HelloCrmServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            System.out.println("\n⚠️  Получен сигнал завершения...");
            server.stop();
        }));

        server.start();

        System.out.println("🔄 Сервер ожидает запросы...");
        Thread.currentThread().join();

    }
}
