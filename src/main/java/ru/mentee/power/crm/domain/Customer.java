package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier){
    private static final String[] VALID_TIERS = {"BRONZE", "SILVER", "GOLD"};

    public Customer{
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact не может быть null");
        }
        if (billingAddress == null) {
            throw new IllegalArgumentException("Платёжный адрес не может быть null");
        }
        if (loyaltyTier == null) {
            throw new IllegalArgumentException("Уровень лояльности не может быть null");
        }
        boolean isValidTier = false;
        for (String validTier : VALID_TIERS) {
            if (validTier.equals(loyaltyTier)) {
                isValidTier = true;
                break;
            }
        }

        if (!isValidTier) {
            throw new IllegalArgumentException(
                    "Недопустимый уровень лояльности: '" + loyaltyTier + "'. " + "Допустимые значения: BRONZE, SILVER, GOLD");
        }
    }


}
