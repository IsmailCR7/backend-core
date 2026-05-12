package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Deal;
import ru.mentee.power.crm.model.DealStatus;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.DealService;
import ru.mentee.power.crm.spring.service.LeadService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Mock
    private DealService dealService;

    @Mock
    private LeadService leadService;

    @Mock
    private Model model;

    @InjectMocks
    private DealController dealController;

    private UUID testLeadId;
    private UUID testDealId;
    private Lead testLead;
    private Deal testDeal;
    private BigDecimal testAmount;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        testLeadId = UUID.randomUUID();
        testDealId = UUID.randomUUID();
        testAmount = new BigDecimal("10000.00");
        now = LocalDateTime.now();

        // Используем правильный конструктор Lead: (id, email, company, status, createdAt)
        testLead = new Lead(testLeadId, "test@example.com", "Test Company", LeadStatus.NEW);

        // Используем правильный конструктор Deal: (id, leadId, amount, status, createdAt)
        testDeal = new Deal(testDealId, testLeadId, testAmount, DealStatus.NEW, now);
    }

    @Test
    void listDealsShouldAddDealsToModelAndReturnView() {
        // Given
        List<Deal> deals = List.of(testDeal);
        when(dealService.getAllDeals()).thenReturn(deals);

        // When
        String viewName = dealController.listDeals(model);

        // Then
        verify(model).addAttribute("deals", deals);
        assertThat(viewName).isEqualTo("deals/list");
    }

    @Test
    void listDealsWhenNoDealsShouldReturnEmptyList() {
        // Given
        when(dealService.getAllDeals()).thenReturn(List.of());

        // When
        String viewName = dealController.listDeals(model);

        // Then
        verify(model).addAttribute("deals", List.of());
        assertThat(viewName).isEqualTo("deals/list");
    }

    @Test
    void kanbanViewShouldAddDealsByStatusToModelAndReturnView() {
        // Given
        Map<DealStatus, List<Deal>> dealsByStatus = new HashMap<>();
        dealsByStatus.put(DealStatus.NEW, List.of(testDeal));
        when(dealService.getDealsByStatusForKanban()).thenReturn(dealsByStatus);

        // When
        String viewName = dealController.kanbanView(model);

        // Then
        verify(model).addAttribute("dealsByStatus", dealsByStatus);
        assertThat(viewName).isEqualTo("deals/kanban");
    }

    @Test
    void kanbanViewWhenEmptyShouldReturnEmptyMap() {
        // Given
        when(dealService.getDealsByStatusForKanban()).thenReturn(new HashMap<>());

        // When
        String viewName = dealController.kanbanView(model);

        // Then
        verify(model).addAttribute("dealsByStatus", new HashMap<>());
        assertThat(viewName).isEqualTo("deals/kanban");
    }

    @Test
    void showConvertFormWhenLeadExistsShouldAddLeadToModelAndReturnView() {
        // Given
        when(leadService.findById(testLeadId)).thenReturn(Optional.of(testLead));

        // When
        String viewName = dealController.showConvertForm(testLeadId, model);

        // Then
        verify(model).addAttribute("lead", testLead);
        assertThat(viewName).isEqualTo("deals/convert");
    }

    @Test
    void showConvertFormWhenLeadNotFoundShouldThrowNotFoundException() {
        // Given
        when(leadService.findById(testLeadId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> dealController.showConvertForm(testLeadId, model))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseEx = (ResponseStatusException) ex;
                    assertThat(responseEx.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    void convertLeadToDealShouldCallServiceAndRedirect() {
        // When
        String redirectUrl = dealController.convertLeadToDeal(testLeadId, testAmount);

        // Then
        verify(dealService).convertLeadToDeal(testLeadId, testAmount);
        assertThat(redirectUrl).isEqualTo("redirect:/deals");
    }

    @Test
    void convertLeadToDealWithZeroAmountShouldStillCallService() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // When
        String redirectUrl = dealController.convertLeadToDeal(testLeadId, zeroAmount);

        // Then
        verify(dealService).convertLeadToDeal(testLeadId, zeroAmount);
        assertThat(redirectUrl).isEqualTo("redirect:/deals");
    }

    @Test
    void convertLeadToDealWithNegativeAmountShouldStillCallService() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-1000.00");

        // When
        String redirectUrl = dealController.convertLeadToDeal(testLeadId, negativeAmount);

        // Then
        verify(dealService).convertLeadToDeal(testLeadId, negativeAmount);
        assertThat(redirectUrl).isEqualTo("redirect:/deals");
    }

    @Test
    void transitionStatusShouldCallServiceAndRedirectToKanban() {
        // Given
        DealStatus newStatus = DealStatus.QUALIFIED;

        // When
        String redirectUrl = dealController.transitionStatus(testDealId, newStatus);

        // Then
        verify(dealService).transitionDealStatus(testDealId, newStatus);
        assertThat(redirectUrl).isEqualTo("redirect:/deals/kanban");
    }

    @Test
    void transitionStatusWithWonStatusShouldCallService() {
        // Given
        DealStatus newStatus = DealStatus.WON;

        // When
        String redirectUrl = dealController.transitionStatus(testDealId, newStatus);

        // Then
        verify(dealService).transitionDealStatus(testDealId, newStatus);
        assertThat(redirectUrl).isEqualTo("redirect:/deals/kanban");
    }

    @Test
    void transitionStatusWithLostStatusShouldCallService() {
        // Given
        DealStatus newStatus = DealStatus.LOST;

        // When
        String redirectUrl = dealController.transitionStatus(testDealId, newStatus);

        // Then
        verify(dealService).transitionDealStatus(testDealId, newStatus);
        assertThat(redirectUrl).isEqualTo("redirect:/deals/kanban");
    }

    @Test
    void showConvertFormShouldHandleMultipleCalls() {
        // Given
        UUID secondLeadId = UUID.randomUUID();
        Lead secondLead = new Lead(secondLeadId, "second@example.com", "Second Company", LeadStatus.CONTACTED);

        when(leadService.findById(testLeadId)).thenReturn(Optional.of(testLead));
        when(leadService.findById(secondLeadId)).thenReturn(Optional.of(secondLead));

        // When
        String viewName1 = dealController.showConvertForm(testLeadId, model);
        String viewName2 = dealController.showConvertForm(secondLeadId, model);

        // Then
        verify(model).addAttribute("lead", testLead);
        verify(model).addAttribute("lead", secondLead);
        assertThat(viewName1).isEqualTo("deals/convert");
        assertThat(viewName2).isEqualTo("deals/convert");
    }
}
