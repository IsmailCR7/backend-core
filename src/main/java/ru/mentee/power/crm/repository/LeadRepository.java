package ru.mentee.power.crm.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID> {

    // ===== 1. DERIVED METHODS (по имени метода) =====

    // Точный поиск по email (уже есть)
    Optional<Lead> findByEmail(String email);

    // Поиск по статусу (уже есть, но используй LeadStatus, а не String!)
    List<Lead> findByStatus(LeadStatus status);

    // Поиск по компании
    List<Lead> findByCompany(String company);

    // Подсчёт лидов по статусу
    long countByStatus(LeadStatus status);

    // Проверка существования по email
    boolean existsByEmail(String email);

    // Поиск по части email (LIKE)
    List<Lead> findByEmailContaining(String emailPart);

    // Поиск по статусу И компании
    List<Lead> findByStatusAndCompany(LeadStatus status, String company);

    // Поиск по статусу с сортировкой по дате создания (новые сверху)
    List<Lead> findByStatusOrderByCreatedAtDesc(LeadStatus status);


    // ===== 2. МЕТОДЫ С ПАГИНАЦИЕЙ =====

    // Все лиды с пагинацией (переопределяем из JpaRepository)
    Page<Lead> findAll(Pageable pageable);

    // Поиск по статусу с пагинацией
    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);

    // Поиск по компании с пагинацией
    Page<Lead> findByCompany(String company, Pageable pageable);

    // ===== 3. JPQL ЗАПРОСЫ =====

    // Поиск по списку статусов (IN запрос)
    @Query("SELECT l FROM Lead l WHERE l.status IN :statuses")
    List<Lead> findByStatusIn(@Param("statuses") List<LeadStatus> statuses);

    // Поиск лидов, созданных после определённой даты
    @Query("SELECT l FROM Lead l WHERE l.createdAt > :date")
    List<Lead> findCreatedAfter(@Param("date") LocalDateTime date);

    // Поиск по компании с сортировкой в JPQL
    @Query("SELECT l FROM Lead l WHERE l.company = :company ORDER BY l.createdAt DESC")
    List<Lead> findByCompanyOrderedByDate(@Param("company") String company);

    // JPQL с пагинацией
    @Query("SELECT l FROM Lead l WHERE l.status IN :statuses")
    Page<Lead> findByStatusInPaged(@Param("statuses") List<LeadStatus> statuses, Pageable pageable);

    // ===== 4. BULK ОПЕРАЦИИ (@Modifying) =====

    // Массовое обновление статуса
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Lead l SET l.status = :newStatus WHERE l.status = :oldStatus")
    int updateStatusBulk(
            @Param("oldStatus") LeadStatus oldStatus,
            @Param("newStatus") LeadStatus newStatus
    );

    // Массовое удаление по статусу
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Lead l WHERE l.status = :status")
    int deleteByStatusBulk(@Param("status") LeadStatus status);


    //Native-запросы
    @Query(value = "SELECT * FROM leads WHERE email = ?1", nativeQuery = true)
    public Optional<Lead> findByEmailNative(String email);

    @Query(value = "SELECT * FROM leads WHERE status = ?1", nativeQuery = true)
    public List<Lead> findByStatusNative(String status);

    // Pessimistic lock для критических операций (конверсия Lead→Deal)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lead l WHERE l.id = :id")
    Optional<Lead> findByIdForUpdate(@Param("id") UUID id);

    // Pessimistic lock для блокировки по email
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lead l WHERE l.email = :email")
    Optional<Lead> findByEmailForUpdate(@Param("email") String email);

}