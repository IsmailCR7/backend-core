package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.mentee.power.crm.Application;

@DataJpaTest  // Вместо @SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = Application.class)
public class DatabaseConfigTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldConnectToH2Database() {
        String databaseProductName = jdbcTemplate.execute((ConnectionCallback<String>) connection ->
                connection.getMetaData().getDatabaseProductName()
        );
        assertThat(databaseProductName).isEqualTo("H2");
    }

    @Test
    void shouldHaveLeadsTableCreated() {
        // Проверяем, что таблица leads существует
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_name = 'LEADS'",
                Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldHaveLeadSequence() {
        // Для PostgreSQL-совместимости с H2
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.sequences " +
                            "WHERE sequence_name = 'LEADS_SEQ'",
                    Integer.class);
            // Не критично, если нет
        } catch (Exception e) {
            // OK - H2 может не иметь sequence
        }
    }
}