package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class CustomerTest {

    private Address mainAddress;
    private Address billingAddress;
    private Contact contact;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        mainAddress = new Address("Москва", "Тверская", "101000");
        billingAddress = new Address("Москва", "Арбат", "121000");
        contact = new Contact("ivan@mail.ru", "+7 999 123-45-67", mainAddress);
        customerId = UUID.randomUUID();
    }

    @Test
    void shouldCreateCustomerWithValidData() {
        Customer customer = new Customer(customerId, contact, billingAddress, "BRONZE");

        assertThat(customer.id()).isEqualTo(customerId);
        assertThat(customer.contact()).isEqualTo(contact);
        assertThat(customer.billingAddress()).isEqualTo(billingAddress);
        assertThat(customer.loyaltyTier()).isEqualTo("BRONZE");
    }



    @Test
    void shouldReuseContactWhenCreatingCustomer() {
        Customer customer = new Customer(
                UUID.randomUUID(),
                contact,
                billingAddress,
                "BRONZE");

        assertThat(customer.contact().address())
                .isNotEqualTo(customer.billingAddress());
        assertThat(customer.contact().address().city()).isEqualTo("Москва");
        assertThat(customer.billingAddress().city()).isEqualTo("Москва");
        assertThat(customer.contact().address().street()).isEqualTo("Тверская");
        assertThat(customer.billingAddress().street()).isEqualTo("Арбат");
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() ->
                new Customer(null, contact, billingAddress, "BRONZE")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID не может быть null");
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        assertThatThrownBy(() ->
                new Customer(customerId, null, billingAddress, "BRONZE")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Contact не может быть null");
    }

    @Test
    void shouldThrowExceptionWhenBillingAddressIsNull() {
        assertThatThrownBy(() ->
                new Customer(customerId, contact, null, "BRONZE")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Платёжный адрес не может быть null");
    }

    @Test
    void shouldThrowExceptionWhenLoyaltyTierIsNull() {
        assertThatThrownBy(() ->
                new Customer(customerId, contact, billingAddress, null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Уровень лояльности не может быть null");
    }

    @Test
    void shouldThrowExceptionWhenLoyaltyTierIsInvalid() {
        assertThatThrownBy(() ->
                new Customer(customerId, contact, billingAddress, "PLATINUM")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимый уровень лояльности")
                .hasMessageContaining("PLATINUM")
                .hasMessageContaining("BRONZE, SILVER, GOLD");
    }

    @Test
    void shouldAcceptAllValidLoyaltyTiers() {
        assertThatCode(() ->
                new Customer(customerId, contact, billingAddress, "BRONZE")
        ).doesNotThrowAnyException();

        assertThatCode(() ->
                new Customer(customerId, contact, billingAddress, "SILVER")
        ).doesNotThrowAnyException();

        assertThatCode(() ->
                new Customer(customerId, contact, billingAddress, "GOLD")
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
        Lead lead = new Lead(
                UUID.randomUUID(),
                contact,
                "ООО Ромашка",
                "NEW");

        Customer customer = new Customer(
                UUID.randomUUID(),
                contact,
                mainAddress,
                "BRONZE");

        assertThat(lead.contact()).isSameAs(contact);
        assertThat(customer.contact()).isSameAs(contact);
        assertThat(lead.contact().email()).isEqualTo("ivan@mail.ru");
        assertThat(customer.contact().email()).isEqualTo("ivan@mail.ru");
        assertThat(lead.contact().address().city()).isEqualTo("Москва");
        assertThat(customer.contact().address().city()).isEqualTo("Москва");
    }
}