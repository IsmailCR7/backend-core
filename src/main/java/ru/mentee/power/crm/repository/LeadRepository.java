package ru.mentee.power.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepository extends JpaRepository<Lead, UUID> {

    // Автоматическая генерация SQL (derived query)
    Optional<Lead> findByEmail(String email);

    // Автоматическая генерация по нескольким полям
    List<Lead> findByStatus(LeadStatus status);

    // Native query для полного контроля
    @Query(value = "SELECT * FROM leads WHERE email = ?1", nativeQuery = true)
    Optional<Lead> findByEmailNative(String email);

    // Native query с именованным параметром
    @Query(value = "SELECT * FROM leads WHERE status = :status ORDER BY created_at DESC",
            nativeQuery = true)
    List<Lead> findByStatusNative(@Param("status") String status);

    // Aggregate функция через native query
    @Query(value = "SELECT COUNT(*) FROM leads WHERE status = ?1", nativeQuery = true)
    long countByStatusNative(String status);
}