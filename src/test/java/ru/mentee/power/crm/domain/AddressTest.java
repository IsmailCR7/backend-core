package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {
    @Test
    void shouldCreateAddresWhenValidData(){
        Address address = new Address("San Francisco", "123 Main St", "94105");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.street()).isEqualTo("123 Main St");
        assertThat(address.zip()).isEqualTo("94105");
    }
    @Test
    void shouldBeEqualWhenSameData(){
        Address firstAddress = new Address("San Francisco", "123 Main St", "94105");
        Address secondAddress = new Address("San Francisco", "123 Main St", "94105");
        assertThat(firstAddress).isEqualTo(secondAddress);
        assertThat(firstAddress.hashCode()).isEqualTo(secondAddress.hashCode());
        assertThat(secondAddress).isEqualTo(firstAddress);
    }
    @Test
    void shouldNotBeEqualWhenDifferentData(){
        Address firstAddress = new Address("SanFrancisco", "1234 Main St", "941056");
        Address secondAddress = new Address("San Francisco", "123 Main St", "94105");
        assertThat(firstAddress).isNotEqualTo(secondAddress);
        assertThat(secondAddress).isNotEqualTo(firstAddress);
    }
    @Test
    void shouldThrowExceptionWhenCityNull(){
        assertThatThrownBy(() -> new Address(null, "Moskovskaya", "12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Город не может быть null или пустым");
        assertThatThrownBy(() -> new Address("", "Moskovskaya", "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Город не может быть null или пустым");
    }
    @Test
    void shouldThrowExceptionWhenZipNull() {
       assertThatThrownBy(() -> new Address("Saint", "Moskovskaya", ""))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessageContaining("Почтовый индекс не может быть null или пустым");
        assertThatThrownBy(() -> new Address("Saint", "Moskovskaya", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Почтовый индекс не может быть null или пустым");

    }


}
