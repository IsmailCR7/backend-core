package ru.mentee.power.crm.domain;

public record Address(String city, String street, String zip) {
    public Address {
        if (city == null || city.trim().isEmpty()){
            throw new IllegalArgumentException("Город не может быть null или пустым");
        }
        if (zip == null || zip.trim().isEmpty()){
            throw new IllegalArgumentException("Почтовый индекс не может быть null или пустым");
        }

    }

}
