package ru.mentee.power.crm.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public record Lead(
        UUID id,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может быть длиннее 255 символов")
        String email,

        @NotBlank(message = "Название компании обязательно")
        @Size(min = 2, max = 200, message = "Название компании должно быть от 2 до 200 символов")
        String company,

        @NotNull(message = "Статус обязателен")
        LeadStatus status
) {

    // Конструктор для создания НОВОГО лида (без ID)
    public Lead(String email, String company, LeadStatus status) {
        this(UUID.randomUUID(), email, company, status);
    }

    // Канонический конструктор для Spring (с ID, может быть null при создании)
    public Lead(UUID id, String email, String company, LeadStatus status) {
        // ID может быть null только когда Spring создаёт объект из формы
        // В этом случае мы сгенерируем новый ID позже
        this.id = id;

        // Эти поля НЕ должны быть null
        this.email = Objects.requireNonNull(email, "Email не должен быть null");
        this.company = Objects.requireNonNull(company, "Company не должен быть null");
        this.status = Objects.requireNonNull(status, "Status не должен быть null");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id) &&
                Objects.equals(email, lead.email) &&
                Objects.equals(company, lead.company) &&
                status == lead.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, company, status);
    }
}