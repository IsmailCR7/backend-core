package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.Test;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

class PostgresConnectionTest {

    @Test
    void testPostgresConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/crm";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "postgres")) {
            System.out.println("✅ Connected to: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("✅ Version: " + conn.getMetaData().getDatabaseProductVersion());

            // Проверяем таблицы
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM leads")) {
                    rs.next();
                    int count = rs.getInt(1);
                    System.out.println("✅ Found " + count + " records in leads table");
                }
            }
        }
    }
}
