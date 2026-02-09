package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {
    @Test
    void shouldReturnIdWhenGetIdCalled() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String id = lead.getId();
        assertThat(id).isEqualTo("A1");
    }

    @Test
    void shouldReturnEmailWhenGetEmail() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String email = lead.getEmail();
        assertThat(email).isEqualTo("davidov-ismail@mail.ru");

    }

    @Test
    void shouldReturnPhoneWhenGetPhone() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String phone = lead.getPhone();
        assertThat(phone).isEqualTo("+79992404769");
    }

    @Test
    void shouldReturnCompanyWhenGetCompany() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String company = lead.getCompany();
        assertThat(company).isEqualTo("Pobeda");
    }

    @Test
    void shouldReturnStatusWhenGetStatus() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String status = lead.getStatus();
        assertThat(status).isEqualTo("flyAttendant");
    }

    @Test
    void shouldReturnFormattedStringWhenToStringCalled() {
        Lead lead = new Lead("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
        String result = lead.toString();
        assertThat(result).contains("A1", "davidov-ismail@mail.ru", "+79992404769", "Pobeda", "flyAttendant");
    }

}
