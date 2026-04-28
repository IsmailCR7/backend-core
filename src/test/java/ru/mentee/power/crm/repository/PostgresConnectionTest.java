package ru.mentee.power.crm.repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import java.sql.DriverManager;
import java.sql.Connection;

class PostgresConnectionTest {

    @Test
    void testPostgresConnection() throws Exception {
        // Пропускаем тест в CI, если PostgreSQL не доступен
        Assumptions.assumeTrue(isPostgresAvailable(), "PostgreSQL not available, skipping test");

        String url = "jdbc:postgresql://localhost:5432/crm";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "postgres")) {
            System.out.println("✅ Connected to: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("✅ Version: " + conn.getMetaData().getDatabaseProductVersion());
        }
    }

    private boolean isPostgresAvailable() {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/crm", "postgres", "postgres")) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("PostgreSQL not available: " + e.getMessage());
            return false;
        }
    }
}
