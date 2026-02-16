package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {
    @Test
    void shouldCreateAddresWhenValidData(){
        Address addres = new Address("San Francisco", "123 Main St", "94105");
        assertThat(addres.city()).isEqualTo("San Francisco");
        assertThat(addres.street()).isEqualTo("123 Main St");
        assertThat(addres.zip()).isEqualTo("94105");
    }
    @Test
    void shouldBeEqualWhenSameData(){
        Address firstAddres = new Address("San Francisco", "123 Main St", "94105");
        Address secondAddres = new Address("San Francisco", "123 Main St", "94105");
        assertThat(firstAddres).isEqualTo(secondAddres);
        assertThat(firstAddres.hashCode()).isEqualTo(secondAddres.hashCode());
        assertThat(secondAddres).isEqualTo(firstAddres);
    }
    @Test
    void shouldNotBeEqualWhenDifferentData(){
        Address firstAddres = new Address("SanFrancisco", "1234 Main St", "941056");
        Address secondAddres = new Address("San Francisco", "123 Main St", "94105");
        assertThat(firstAddres).isNotEqualTo(secondAddres);
        assertThat(secondAddres).isNotEqualTo(firstAddres);
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
