package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {
    @Test
    void shouldCreateContactWhenValidData(){
        Contact contact = new Contact("Ismail", "Davydov", "davidov-ismail@mail.ru");
        assertThat(contact.firstName()).isEqualTo("Ismail");
        assertThat(contact.lastName()).isEqualTo("Davydov");
        assertThat(contact.email()).isEqualTo("davidov-ismail@mail.ru");
    }
    @Test
    void shouldBeEqualWhenSameData() {
        Contact contact1 = new Contact("Ismail", "Davydov", "davidov-ismail@mail.ru");
        Contact contact2 = new Contact("Ismail", "Davydov", "davidov-ismail@mail.ru");
        assertThat(contact1).isEqualTo(contact2);
        assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
        assertThat(contact2).isEqualTo(contact1);

    }
    @Test
    void shouldNotBeEqualWhenDifferentData(){
        Contact contact1 = new Contact("Ismail", "Davydov", "davidov-ismail@mail.ru");
        Contact contact2 = new Contact("Iskander", "Davidov", "davidovismail@mail.ru");
        assertThat(contact1).isNotEqualTo(contact2);


    }

}
