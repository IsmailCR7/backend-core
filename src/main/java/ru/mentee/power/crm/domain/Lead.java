package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.UUID;

public record Lead(UUID id, Contact contact, String company, String status) {
    private static final String[] VALID_STATUSES = {"NEW", "QUALIFIED", "CONVERTED"};


    public Lead {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact не может быть null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status не может быть null");
        }

    }
}
