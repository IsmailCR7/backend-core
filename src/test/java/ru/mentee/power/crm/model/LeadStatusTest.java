package ru.mentee.power.crm.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeadStatusTest {

    @Test
    void testEnumValues() {
        // Проверяем количество значений
        assertEquals(5, LeadStatus.values().length);

        // Проверяем наличие всех ожидаемых значений
        assertTrue(containsConstant(LeadStatus.NEW));
        assertTrue(containsConstant(LeadStatus.CONTACTED));
        assertTrue(containsConstant(LeadStatus.QUALIFIED));
        assertTrue(containsConstant(LeadStatus.LOST));
        assertTrue(containsConstant(LeadStatus.CONFIDENTIAL));
    }

    @Test
    void testEnumOrder() {
        // Проверяем порядок объявления
        LeadStatus[] values = LeadStatus.values();
        assertEquals(LeadStatus.NEW, values[0]);
        assertEquals(LeadStatus.CONTACTED, values[1]);
        assertEquals(LeadStatus.QUALIFIED, values[2]);
        assertEquals(LeadStatus.LOST, values[3]);
        assertEquals(LeadStatus.CONFIDENTIAL, values[4]);
    }

    @Test
    void testValueOf() {
        // Проверяем преобразование из строки
        assertEquals(LeadStatus.NEW, LeadStatus.valueOf("NEW"));
        assertEquals(LeadStatus.CONTACTED, LeadStatus.valueOf("CONTACTED"));
        assertEquals(LeadStatus.QUALIFIED, LeadStatus.valueOf("QUALIFIED"));
        assertEquals(LeadStatus.LOST, LeadStatus.valueOf("LOST"));
        assertEquals(LeadStatus.CONFIDENTIAL, LeadStatus.valueOf("CONFIDENTIAL"));
    }

    @Test
    void testValueOfInvalid() {
        // Проверяем, что невалидное значение выбрасывает исключение
        assertThrows(IllegalArgumentException.class, () -> {
            LeadStatus.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testToString() {
        // Проверяем строковое представление
        assertEquals("NEW", LeadStatus.NEW.toString());
        assertEquals("CONTACTED", LeadStatus.CONTACTED.toString());
        assertEquals("QUALIFIED", LeadStatus.QUALIFIED.toString());
        assertEquals("LOST", LeadStatus.LOST.toString());
        assertEquals("CONFIDENTIAL", LeadStatus.CONFIDENTIAL.toString());
    }

    @Test
    void testName() {
        // Проверяем метод name()
        assertEquals("NEW", LeadStatus.NEW.name());
        assertEquals("CONTACTED", LeadStatus.CONTACTED.name());
    }

    private boolean containsConstant(LeadStatus status) {
        for (LeadStatus s : LeadStatus.values()) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}