package ru.mentee.power.crm.web;

import org.junit.jupiter.api.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HelloCrmServerTest {

    private static HelloCrmServer server;
    private static final int TEST_PORT = 8089;

    @BeforeAll
    static void setUp() throws Exception {
        server = new HelloCrmServer(TEST_PORT);
        server.start();
        Thread.sleep(1000);
    }

    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    @DisplayName("Интеграционный тест: Проверка через HttpURLConnection")
    void testWithHttpURLConnection() throws Exception {
        URL url = new URL("http://localhost:" + TEST_PORT + "/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        String contentType = connection.getContentType();
        assertTrue(contentType.contains("text/html"));

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String response = reader.lines().collect(Collectors.joining("\n"));
            assertTrue(response.contains("Hello CRM"));
            System.out.println("Ответ через HttpURLConnection: " + response.substring(0, 50) + "...");
        }

        connection.disconnect();
        System.out.println("✅ Интеграционный тест с HttpURLConnection пройден");
    }

    @Test
    @DisplayName("Стресс-тест: Множество параллельных запросов")
    void testConcurrentRequests() throws Exception {
        int threadCount = 10;
        int requestsPerThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        URL url = new URL("http://localhost:" + TEST_PORT + "/hello");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        int responseCode = connection.getResponseCode();
                        assertEquals(200, responseCode);

                        connection.disconnect();

                        Thread.sleep(50);
                    }
                } catch (Exception e) {
                    fail("Ошибка при параллельном запросе: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);
        assertTrue(finished, "Все запросы должны завершиться за 30 секунд");

        System.out.println("✅ Стресс-тест пройден: " + (threadCount * requestsPerThread) +
                " параллельных запросов обработано");
    }

    @Test
    @DisplayName("Тест: Проверка времени ответа")
    void testResponseTime() throws Exception {
        long startTime = System.currentTimeMillis();

        URL url = new URL("http://localhost:" + TEST_PORT + "/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        long endTime = System.currentTimeMillis();

        long responseTime = endTime - startTime;

        assertEquals(200, responseCode);
        assertTrue(responseTime < 1000, "Время ответа должно быть меньше 1 секунды, было: " + responseTime + "ms");

        connection.disconnect();

        System.out.println("✅ Тест времени ответа пройден: " + responseTime + "ms");
    }
}
