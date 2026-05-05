package ru.mentee.power.crm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leads")
@Data                           // ← ОДНА аннотация заменяет 50+ строк кода!
@NoArgsConstructor              // Пустой конструктор (нужен JPA)
@AllArgsConstructor             // Конструктор со всеми полями
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 255, message = "Email не может быть длиннее 255 символов")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Название компании обязательно")
    @Size(min = 2, max = 200, message = "Название компании должно быть от 2 до 200 символов")
    @Column(nullable = false, length = 200)
    private String company;

    @NotNull(message = "Статус обязателен")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 👇 Только КАСТОМНЫЕ конструкторы (которые Lombok не умеет генерировать)

    // Для создания НОВОГО лида (без ID, с авто-генерацией createdAt)
    public Lead(String email, String company, LeadStatus status) {
        this.email = email;
        this.company = company;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        // id сгенерируется автоматически через @GeneratedValue
    }
}


