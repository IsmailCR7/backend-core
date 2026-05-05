package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;

    @GetMapping
    @ResponseBody
    public String home() {
        return "Spring Boot CRM is running! Leads count: " + leadService.findAll().size();
    }

    @GetMapping("/leads/new")
    public String showCreateForm(Model model) {
        model.addAttribute("lead", new Lead());
        model.addAttribute("statuses", LeadStatus.values());
        return "leads/create";
    }

    @GetMapping("/leads")
    public String showLeads(@RequestParam(required = false) String search,
                            @RequestParam(required = false) LeadStatus status,
                            Model model) {
        List<Lead> leads;
        if (search != null && !search.isEmpty()) {
            leads = leadService.searchByNameOrEmail(search, status);
        } else {
            leads = leadService.findByStatus(status);
        }

        model.addAttribute("leads", leads);
        model.addAttribute("search", search);
        model.addAttribute("currentFilter", status);
        return "leads/list";
    }

    @PostMapping("/leads")
    public String createLead(@Valid @ModelAttribute("lead") Lead lead,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", LeadStatus.values());
            return "leads/create";
        }
        try {
            leadService.addLead(lead);
            return "redirect:/leads";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statuses", LeadStatus.values());
            return "leads/create";
        }
    }

    @GetMapping("/leads/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Lead lead = leadService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cannot find lead with id " + id));
        model.addAttribute("lead", lead);
        model.addAttribute("statuses", LeadStatus.values());
        return "leads/edit";
    }

    @PostMapping("/leads/{id}")
    public String updateLead(@PathVariable UUID id,
                             @Valid @ModelAttribute("lead") Lead lead,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", LeadStatus.values());
            return "leads/edit";
        }
        leadService.update(id, lead);
        return "redirect:/leads";
    }

    @PostMapping("/leads/{id}/delete")
    public String deleteLead(@PathVariable UUID id) {
        leadService.delete(id);
        return "redirect:/leads";
    }
}