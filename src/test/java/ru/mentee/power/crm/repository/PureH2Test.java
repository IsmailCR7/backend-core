package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.Test;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

class PureH2Test {
    
    @Test
    void testH2WithPostgreSQLMode() throws Exception {
        String url = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            System.out.println("✅ Connected to: " + conn.getMetaData().getDatabaseProductName());
            
            try (Statement stmt = conn.createStatement()) {
                // Проверяем существует ли таблица
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'LEADS'");
                rs.next();
                boolean tableExists = rs.getInt(1) > 0;
                
                if (!tableExists) {
                    stmt.execute("""
                        CREATE TABLE leads (
                            id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            company VARCHAR(255) NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        )
                    """);
                    System.out.println("✅ Table 'leads' created");
                } else {
                    System.out.println("✅ Table 'leads' already exists, skipping creation");
                }
                
                // Очищаем таблицу перед вставкой
                stmt.execute("DELETE FROM leads");
                
                // Вставляем данные
                int inserted = stmt.executeUpdate("""
                    INSERT INTO leads (email, company, status) VALUES 
                        ('ivan@example.com', 'ООО ТехноСервис', 'NEW'),
                        ('elena@example.com', 'Альфа Групп', 'CONTACTED')
                """);
                System.out.println("✅ Inserted " + inserted + " records");
                
                // Проверяем
                try (ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM leads")) {
                    rs2.next();
                    int count = rs2.getInt(1);
                    System.out.println("✅ Found " + count + " record(s) in leads table");
                }
            }
        }
    }
}
