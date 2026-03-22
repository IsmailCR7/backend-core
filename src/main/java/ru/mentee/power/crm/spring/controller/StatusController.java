package ru.mentee.power.crm.spring.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mentee.power.crm.model.Status;
import ru.mentee.power.crm.service.StatusService;

@Controller
public class StatusController {
    private final StatusService service;
    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping("/statuses")
    public String showStatus(Model model) {
        List<Status> statuses = service.findAll();
        model.addAttribute("statuses", statuses);
        return "status/list";
    }

    @GetMapping("/statuses/new")
    public String showCreateStatusForm(Model model) {
        model.addAttribute("status", new Status(""));
        return "status/create";
    }

    @PostMapping("/statuses")
    public String createStatus(@RequestParam String status) {
        if (service.containsStatus(status)) {
            LOG.warn("Попытка добавить дублирующий статус: {}", status);
        } else {
            service.addStatus(new Status(status));
            LOG.info("Статус успешно создан: {}", status);
        }
        return "redirect:/statuses";
    }
}
