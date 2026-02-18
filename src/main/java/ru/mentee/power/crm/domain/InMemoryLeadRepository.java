package ru.mentee.power.crm.domain;
import java.util.*;

public class InMemoryLeadRepository implements Repository<Lead>{
    // Используем ArrayList для хранения лидов
    private final List<Lead> leads = new ArrayList<>();

    @Override
    public void add(Lead lead) {
        // Проверяем, есть ли уже такой Лид (дедупликация)
        if (!leads.contains(lead)) {
            leads.add(lead);
        }
        // Если Лид уже существует - просто игнорируем добавление
    }

    @Override
    public void remove(UUID id) {
        // Находим Лид по id и удаляем
        leads.removeIf(lead -> lead.id().equals(id));
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        // Ищем Лид по id и возвращаем Optional
        return leads.stream()
                .filter(lead -> lead.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Lead> findAll() {
        // Возвращаем защитную копию (defensive copy)
        return new ArrayList<>(leads);
    }

}
