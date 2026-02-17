package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {

    @Test
    void shouldCreateContactWhenValidData() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");
        Contact contact = new Contact("davidov-smail@mail.ru", "+79992404769", address);

        assertThat(contact.email()).isEqualTo("davidov-smail@mail.ru");
        assertThat(contact.phone()).isEqualTo("+79992404769");
        assertThat(contact.address()).isEqualTo(address);
    }

    @Test
    void shouldBeEqualWhenSameData() {
        Address address1 = new Address("Saint-Peterburg", "Moskovskaya", "1234");
        Address address2 = new Address("Saint-Peterburg", "Moskovskaya", "1234");
        Contact contact1 = new Contact("davidov-smail@mail.ru", "+79992404769", address1);
        Contact contact2 = new Contact("davidov-smail@mail.ru", "+79992404769", address2);

        assertThat(contact1).isEqualTo(contact2);
        assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
        assertThat(contact2).isEqualTo(contact1);
    }

    @Test
    void shouldNotBeEqualWhenDifferentData() {
        Address addressFirst = new Address("Saint-Peterburg", "Moskovskaya", "1234");
        Address addressSecond = new Address("SaintPeterburg", "Moskovskaya1", "12345");
        Contact contactFirst = new Contact("davidovsmail@mail.ru", "+79992404770", addressFirst);
        Contact contactSecond = new Contact("davidov-smail@mail.ru", "+79992404769", addressSecond);

        assertThat(contactFirst).isNotEqualTo(contactSecond);
        assertThat(contactSecond).isNotEqualTo(contactFirst);
    }

    @Test
    void shouldDelegateToAddressWhenAccessingCity() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");
        Contact contact = new Contact("davidovsmail@mail.ru", "+79992404770", address);

        assertThat(contact.address().city()).isEqualTo("Saint-Peterburg");
        assertThat(contact.address().street()).isEqualTo("Moskovskaya");
        assertThat(contact.address().zip()).isEqualTo("1234");
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThatThrownBy(() -> new Contact("test@mail.ru", "+7 999 111-22-33", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Адрес не может быть null");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact(null, "+79992404769", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Значение email не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("", "+79992404769", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Значение email не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("   ", "+79992404769", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Значение email не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotContainAtSymbol() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("invalid-email", "+79992404769", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Некорректный формат email");
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsNull() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("test@mail.ru", null, address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Номер телефона не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsEmpty() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("test@mail.ru", "", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Номер телефона не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsBlank() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("test@mail.ru", "   ", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Номер телефона не может быть null или пустым");
    }

    @Test
    void shouldThrowExceptionWhenPhoneContainsInvalidCharacters() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact("test@mail.ru", "abc123", address))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Телефон может содержать только цифры");
    }

    @Test
    void shouldAcceptValidPhoneFormats() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatCode(() -> new Contact("test@mail.ru", "+79992404769", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("test@mail.ru", "8 999 240-47-69", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("test@mail.ru", "8-999-240-47-69", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("test@mail.ru", "89992404769", address))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldAcceptValidEmailFormats() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatCode(() -> new Contact("user@mail.ru", "+79992404769", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("user.name@mail.ru", "+79992404769", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("user+filter@mail.ru", "+79992404769", address))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Contact("user@yandex.ru", "+79992404769", address))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenAllFieldsAreInvalid() {
        Address address = new Address("Saint-Peterburg", "Moskovskaya", "1234");

        assertThatThrownBy(() -> new Contact(null, null, address))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Contact("", "", address))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Contact("invalid", "abc", address))
                .isInstanceOf(IllegalArgumentException.class);
    }
}