package ru.mentee.power.crm.domain;


public record Contact(String email, String phone, Address address){
    public Contact{
        if (email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Значение email не может быть null или пустым");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        if (phone == null || phone.trim().isEmpty()){
            throw new IllegalArgumentException("Номер телефона не может быть null или пустым");
        }
        if (!phone.matches("[0-9\\s\\-+]+")) {
            throw new IllegalArgumentException("Телефон может содержать только цифры, пробелы, дефисы и знак плюс");
        }
        if (address == null){
            throw new IllegalArgumentException("Адрес не может быть null или быть пустым");
        }
    }
}