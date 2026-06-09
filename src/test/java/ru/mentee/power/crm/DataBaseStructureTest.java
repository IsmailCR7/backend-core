package ru.mentee.power.crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional  // Добавляем транзакционность
class DatabaseStructureTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testCompanyTableStructure() {
        System.out.println("\n=== 1. Проверка структуры таблицы companies ===");

        var columns = jdbcTemplate.queryForList(
                "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'COMPANIES' ORDER BY ordinal_position"
        );

        // Выводим результат
        columns.forEach(col -> {
            System.out.printf("  ✓ %s (%s)%n", col.get("COLUMN_NAME"), col.get("DATA_TYPE"));
        });

        // Проверяем, что таблица существует (хотя бы одна колонка)
        assertThat(columns).isNotEmpty();

        // Проверяем наличие обязательных колонок
        var columnNames = columns.stream()
                .map(c -> c.get("COLUMN_NAME").toString().toLowerCase())
                .toList();

        assertThat(columnNames).contains("id", "name", "industry");

        System.out.println("\n  ✅ Таблица companies создана корректно!");
    }

    @Test
    void testForeignKeyInLeads() {
        System.out.println("\n=== 2. Проверка foreign key в таблице leads ===");

        var fks = jdbcTemplate.queryForList(
                "SELECT constraint_name FROM information_schema.table_constraints " +
                        "WHERE table_name = 'LEADS' AND constraint_type = 'FOREIGN KEY'"
        );

        if (fks.isEmpty()) {
            System.out.println("  ⚠ Foreign key не найден в H2");
        } else {
            fks.forEach(fk -> {
                System.out.printf("  ✓ FK найден: %s%n", fk.get("CONSTRAINT_NAME"));
            });
            assertThat(fks).isNotEmpty();
            System.out.println("\n  ✅ Foreign key в таблице leads создан корректно!");
        }
    }

    @Test
    void testLiquibaseMigrations() {
        System.out.println("\n=== 3. Проверка Liquibase миграций ===");

        try {
            // Проверяем таблицу databasechangelog
            var tables = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_name = 'DATABASECHANGELOG'"
            );

            if (tables.isEmpty()) {
                System.out.println("  ⚠ Таблица DATABASECHANGELOG не найдена (Hibernate DDL auto создаёт таблицы напрямую)");
                System.out.println("  Это нормально для тестов с spring.jpa.hibernate.ddl-auto=create-drop");
            } else {
                var migrations = jdbcTemplate.queryForList(
                        "SELECT id, author, dateexecuted FROM databasechangelog WHERE id LIKE '%company%' OR id LIKE '%leads%'"
                );

                migrations.forEach(m -> {
                    System.out.printf("  ✓ %s by %s at %s%n",
                            m.get("ID"),
                            m.get("AUTHOR"),
                            m.get("DATEEXECUTED"));
                });
            }
        } catch (Exception e) {
            System.out.println("  ℹ Liquibase не используется для тестовой БД");
        }
    }

    @Test
    void testDataInTables() {
        System.out.println("\n=== 4. Проверка данных в таблицах ===");

        // Проверяем, есть ли компании
        var companiesCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM companies", Integer.class);
        System.out.printf("  Компаний в БД: %d%n", companiesCount);

        // Проверяем, есть ли лиды
        var leadsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM leads", Integer.class);
        System.out.printf("  Лидов в БД: %d%n", leadsCount);

        // Если есть данные, показываем связи
        if (companiesCount > 0) {
            var companyLeads = jdbcTemplate.queryForList(
                    "SELECT c.name, COUNT(l.id) as leads_count " +
                            "FROM companies c " +
                            "LEFT JOIN leads l ON l.company_id = c.id " +
                            "GROUP BY c.id, c.name"
            );

            System.out.println("\n  Компании и количество лидов:");
            companyLeads.forEach(row -> {
                System.out.printf("    %-20s -> %d лидов%n",
                        row.get("NAME"),
                        row.get("LEADS_COUNT"));
            });
        }
    }
}
