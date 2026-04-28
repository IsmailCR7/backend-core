package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Deal;
import ru.mentee.power.crm.model.DealStatus;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryDealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private InMemoryDealRepository dealRepository;

    @Mock
    private LeadRepository leadRepository;

    @InjectMocks
    private DealService dealService;

    private UUID validLeadId;
    private UUID invalidLeadId;
    private UUID validDealId;
    private UUID invalidDealId;
    private BigDecimal amount;
    private Lead testLead;
    private Deal testDeal;

    @BeforeEach
    void setUp() {
        validLeadId = UUID.randomUUID();
        invalidLeadId = UUID.randomUUID();
        validDealId = UUID.randomUUID();
        invalidDealId = UUID.randomUUID();
        amount = new BigDecimal("10000.00");
        testLead = new Lead(validLeadId, "test@example.com", "Test Company", LeadStatus.NEW);
        testDeal = new Deal(validDealId, validLeadId, amount, DealStatus.NEW, null);
    }

    @Test
    void convertLeadToDealWhenLeadExistsShouldCreateAndSaveDeal() {
        when(leadRepository.findById(validLeadId)).thenReturn(Optional.of(testLead));
        doNothing().when(dealRepository).save(any(Deal.class));

        Deal result = dealService.convertLeadToDeal(validLeadId, amount);

        assertThat(result).isNotNull();
        assertThat(result.getLeadId()).isEqualTo(validLeadId);
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getStatus()).isEqualTo(DealStatus.NEW);
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    void convertLeadToDealWhenLeadExistsShouldReturnDealWithCorrectFields() {
        when(leadRepository.findById(validLeadId)).thenReturn(Optional.of(testLead));
        doNothing().when(dealRepository).save(any(Deal.class));

        Deal result = dealService.convertLeadToDeal(validLeadId, amount);

        assertThat(result.getLeadId()).isEqualTo(validLeadId);
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void convertLeadToDealWhenLeadExistsWithDifferentAmountShouldCreateDealWithThatAmount() {
        BigDecimal differentAmount = new BigDecimal("50000.00");
        when(leadRepository.findById(validLeadId)).thenReturn(Optional.of(testLead));
        doNothing().when(dealRepository).save(any(Deal.class));

        Deal result = dealService.convertLeadToDeal(validLeadId, differentAmount);

        assertThat(result.getAmount()).isEqualTo(differentAmount);
    }

    @Test
    void convertLeadToDealWhenLeadNotExistsShouldThrowIllegalArgumentException() {
        when(leadRepository.findById(invalidLeadId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.convertLeadToDeal(invalidLeadId, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lead not found: " + invalidLeadId);

        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void convertLeadToDealWithZeroAmountShouldCreateDeal() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        when(leadRepository.findById(validLeadId)).thenReturn(Optional.of(testLead));
        doNothing().when(dealRepository).save(any(Deal.class));

        Deal result = dealService.convertLeadToDeal(validLeadId, zeroAmount);

        assertThat(result.getAmount()).isEqualTo(zeroAmount);
    }

    @Test
    void transitionDealStatusWhenDealExistsAndValidTransitionShouldUpdateStatus() {
        Deal deal = new Deal(validDealId, validLeadId, amount, DealStatus.NEW, null);
        when(dealRepository.findById(validDealId)).thenReturn(Optional.of(deal));
        doNothing().when(dealRepository).save(any(Deal.class));

        Deal result = dealService.transitionDealStatus(validDealId, DealStatus.QUALIFIED);

        assertThat(result.getStatus()).isEqualTo(DealStatus.QUALIFIED);
        verify(dealRepository, times(1)).save(deal);
    }

    @Test
    void transitionDealStatusWhenTransitionToWonShouldSucceed() {
        Deal deal = new Deal(validDealId, validLeadId, amount, DealStatus.NEW, null);
        when(dealRepository.findById(validDealId)).thenReturn(Optional.of(deal));
        doNothing().when(dealRepository).save(any(Deal.class));

        dealService.transitionDealStatus(validDealId, DealStatus.QUALIFIED);
        dealService.transitionDealStatus(validDealId, DealStatus.PROPOSAL_SENT);
        dealService.transitionDealStatus(validDealId, DealStatus.NEGOTIATION);
        Deal result = dealService.transitionDealStatus(validDealId, DealStatus.WON);

        assertThat(result.getStatus()).isEqualTo(DealStatus.WON);
    }

    @Test
    void transitionDealStatusWhenDealNotExistsShouldThrowIllegalArgumentException() {
        when(dealRepository.findById(invalidDealId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.transitionDealStatus(invalidDealId, DealStatus.QUALIFIED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deal not found: " + invalidDealId);

        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void transitionDealStatusWhenInvalidTransitionShouldThrowIllegalStateException() {
        Deal deal = new Deal(validDealId, validLeadId, amount, DealStatus.NEW, null);
        when(dealRepository.findById(validDealId)).thenReturn(Optional.of(deal));

        assertThatThrownBy(() -> dealService.transitionDealStatus(validDealId, DealStatus.WON))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot transition from NEW to WON");
    }

    @Test
    void getAllDealsShouldReturnAllDeals() {
        List<Deal> deals = List.of(testDeal);
        when(dealRepository.findAll()).thenReturn(deals);

        List<Deal> result = dealService.getAllDeals();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testDeal);
        verify(dealRepository, times(1)).findAll();
    }

    @Test
    void getAllDealsWhenNoDealsShouldReturnEmptyList() {
        when(dealRepository.findAll()).thenReturn(new ArrayList<>());

        List<Deal> result = dealService.getAllDeals();

        assertThat(result).isEmpty();
        verify(dealRepository, times(1)).findAll();
    }

    @Test
    void getAllDealsWithMultipleDealsShouldReturnAll() {
        UUID anotherLeadId = UUID.randomUUID();
        Deal deal2 = new Deal(UUID.randomUUID(), anotherLeadId, new BigDecimal("20000.00"), DealStatus.NEW, null);
        List<Deal> deals = List.of(testDeal, deal2);
        when(dealRepository.findAll()).thenReturn(deals);

        List<Deal> result = dealService.getAllDeals();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(testDeal, deal2);
    }

    @Test
    void getDealsByStatusForKanbanShouldGroupDealsByStatus() {
        Deal newDeal1 = new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.NEW, null);
        Deal newDeal2 = new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("15000.00"), DealStatus.NEW, null);
        Deal qualifiedDeal = new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("20000.00"), DealStatus.QUALIFIED, null);

        List<Deal> deals = List.of(newDeal1, newDeal2, qualifiedDeal);
        when(dealRepository.findAll()).thenReturn(deals);

        Map<DealStatus, List<Deal>> result = dealService.getDealsByStatusForKanban();

        assertThat(result).containsKey(DealStatus.NEW);
        assertThat(result).containsKey(DealStatus.QUALIFIED);
        assertThat(result.get(DealStatus.NEW)).hasSize(2);
        assertThat(result.get(DealStatus.QUALIFIED)).hasSize(1);
    }

    @Test
    void getDealsByStatusForKanbanWhenNoDealsShouldReturnEmptyMap() {
        when(dealRepository.findAll()).thenReturn(new ArrayList<>());

        Map<DealStatus, List<Deal>> result = dealService.getDealsByStatusForKanban();

        assertThat(result).isEmpty();
    }

    @Test
    void getDealsByStatusForKanbanWithAllStatusesShouldGroupCorrectly() {
        List<Deal> deals = new ArrayList<>();

        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.NEW, null));
        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.QUALIFIED, null));
        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.PROPOSAL_SENT, null));
        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.NEGOTIATION, null));
        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.WON, null));
        deals.add(new Deal(UUID.randomUUID(), validLeadId, new BigDecimal("10000.00"), DealStatus.LOST, null));

        when(dealRepository.findAll()).thenReturn(deals);

        Map<DealStatus, List<Deal>> result = dealService.getDealsByStatusForKanban();

        assertThat(result).hasSize(DealStatus.values().length);
        for (DealStatus status : DealStatus.values()) {
            assertThat(result.get(status)).hasSize(1);
        }
    }

    @Test
    void convertLeadToDealWithNullAmountShouldThrowNullPointerException() {
        when(leadRepository.findById(validLeadId)).thenReturn(Optional.of(testLead));

        assertThatThrownBy(() -> dealService.convertLeadToDeal(validLeadId, null))
                .isInstanceOf(NullPointerException.class);
    }
}

