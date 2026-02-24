package ru.mentee.power.crm.model;

public record Lead(
        String id,
        String email,
        String phone,
        String company,
        String status) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return id.equals(lead.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

