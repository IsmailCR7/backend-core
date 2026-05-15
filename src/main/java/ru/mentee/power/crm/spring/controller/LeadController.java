package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@Controller
@RequiredArgsConstructor
public class LeadController {
    private static final Logger LOG = LoggerFactory.getLogger(LeadController.class);
    private final LeadService leadService;

    // ===== ДОМАШНЯЯ СТРАНИЦА =====

    @GetMapping
    @ResponseBody
    public String home() {
        long leadCount = leadService.countByStatus(null); // или leadService.findAll().size()
        return "Spring Boot CRM is running! Leads in Database: " + leadCount + " leads.";
    }

    // ===== ПОКАЗ ВСЕХ ЛИДОВ (с пагинацией и фильтрацией) =====

    /**
     * Показывает список лидов с возможностью фильтрации и пагинации.
     *
     * @param name фильтр по имени (частичное совпадение)
     * @param email фильтр по email (частичное совпадение)
     * @param company фильтр по компании (частичное совпадение)
     * @param status фильтр по статусу (точное совпадение)
     * @param page номер страницы (начиная с 0, по умолчанию 0)
     * @param size размер страницы (по умолчанию 20)
     * @param model модель для передачи данных в представление
     */
    @GetMapping("/leads")
    public String showLeads(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        // Используем улучшенный метод поиска (без фильтрации в памяти)
        List<Lead> leads = leadService.searchLeads(name, email, company, status);

        // Альтернатива с пагинацией (раскомментируй, если хочешь использовать)
        // Page<Lead> leadPage = leadService.findLeadsPaged(name, email, company, status, page, size);

        model.addAttribute("leads", leads);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("company", company);
        model.addAttribute("status", status);
        model.addAttribute("statuses", LeadStatus.values()); // для выпадающего списка

        // Для пагинации (если используешь Page)
        // model.addAttribute("currentPage", page);
        // model.addAttribute("totalPages", leadPage.getTotalPages());
        // model.addAttribute("totalElements", leadPage.getTotalElements());

        LOG.info("Displaying {} leads with filters - name: {}, email: {}, company: {}, status: {}",
                leads.size(), name, email, company, status);

        return "leads/list";
    }

    // ===== СОЗДАНИЕ ЛИДА =====

    /**
     * Показывает форму для создания нового лида.
     */
    @GetMapping("/leads/new")
    public String showCreateForm(Model model) {
        model.addAttribute("lead", new Lead("", "", "", LeadStatus.NEW));
        model.addAttribute("statuses", LeadStatus.values());
        model.addAttribute("isEdit", false); // для определения режима формы
        return "leads/create";
    }

    /**
     * Обрабатывает создание нового лида.
     */
    @PostMapping("/leads")
    public String createLead(@Valid @ModelAttribute Lead lead,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("isEdit", false);
            LOG.warn("Lead creation failed due to validation errors: {}", result.getAllErrors());
            return "leads/create"; // ← исправлено: возвращаем create, а не form
        }

        try {
            leadService.addLead(lead.name(), lead.email(), lead.company(), lead.status());
            LOG.info("Successfully created lead with email: {}", lead.email());
            return "redirect:/leads";
        } catch (IllegalStateException e) {
            // Обработка ошибки дублирования email
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("isEdit", false);
            LOG.error("Failed to create lead: {}", e.getMessage());
            return "leads/create";
        }
    }

    // ===== РЕДАКТИРОВАНИЕ ЛИДА =====

    /**
     * Показывает форму для редактирования лида.
     */
    @GetMapping("/leads/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Lead lead = findLeadOrThrow(id);
        model.addAttribute("lead", lead);
        model.addAttribute("statuses", LeadStatus.values());
        model.addAttribute("isEdit", true);
        return "leads/edit";
    }

    /**
     * Обрабатывает обновление лида.
     */
    @PostMapping("/leads/{id}")
    public String updateLead(@PathVariable UUID id,
                             @Valid @ModelAttribute Lead lead,
                             BindingResult result,
                             Model model) {

        // Проверяем, существует ли лид
        findLeadOrThrow(id);

        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("isEdit", true);
            LOG.warn("Lead update failed for id {} due to validation errors: {}", id, result.getAllErrors());
            return "leads/edit";
        }

        try {
            leadService.update(id, lead);
            LOG.info("Successfully updated lead with id: {}", id);
            return "redirect:/leads";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("isEdit", true);
            LOG.error("Failed to update lead with id {}: {}", id, e.getMessage());
            return "leads/edit";
        }
    }

    // ===== УДАЛЕНИЕ ЛИДА =====

    /**
     * Удаляет лида по ID.
     */
    @PostMapping("/leads/{id}/delete")
    public String deleteLead(@PathVariable UUID id) {
        findLeadOrThrow(id); // проверяем существование
        leadService.delete(id);
        LOG.info("Successfully deleted lead with id: {}", id);
        return "redirect:/leads";
    }

    // ===== API ENDPOINTS (для AJAX/JSON запросов) =====

    /**
     * REST API endpoint для получения лида по ID (возвращает JSON).
     */
    @GetMapping("/api/leads/{id}")
    @ResponseBody
    public Lead getLeadApi(@PathVariable UUID id) {
        return findLeadOrThrow(id);
    }

    /**
     * REST API endpoint для поиска лидов (возвращает JSON).
     */
    @GetMapping("/api/leads/search")
    @ResponseBody
    public List<Lead> searchLeadsApi(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) LeadStatus status) {
        return leadService.searchLeads(name, email, company, status);
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ =====

    /**
     * Находит лида по ID или выбрасывает исключение 404.
     */
    private Lead findLeadOrThrow(UUID id) {
        return leadService.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Lead with id {} not found", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cannot find lead with id " + id
                    );
                });
    }
}