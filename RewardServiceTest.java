package com.home.work.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.home.work.Entity.Transaction;
import com.home.work.Service.RewardService;
import com.home.work.exception.InvalidDateException;
import com.home.work.exception.TransactionNotFoundException;
import com.home.work.model.CustomerRewardsSummary;
import com.home.work.repo.RewardRepo;

@SpringBootTest
public class RewardServiceTest {

    @MockBean
    private RewardRepo rewardRepo;

    @Autowired
    private RewardService rewardService;

    private Transaction transaction1, transaction2, transaction3;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        transaction1 = new Transaction(1, "C123", LocalDate.now().minusDays(5), 120.0);
        transaction2 = new Transaction(2, "C123", LocalDate.now().minusDays(10), 80.0);
        transaction3 = new Transaction(3, "C123", LocalDate.now().minusMonths(4), 200.0); // Invalid due to 3-month rule

        transactions = Arrays.asList(transaction1, transaction2);
    }

    @Test
    void testAddTransaction() {
        when(rewardRepo.save(transaction1)).thenReturn(transaction1); // Mock return value
        rewardService.addTransaction(transaction1);
        verify(rewardRepo, times(1)).save(transaction1);
    }


    @Test
    void testAddTransaction_InvalidFutureDate() {
        Transaction futureTransaction = new Transaction(4, "C123", LocalDate.now().plusDays(5), 100.0);
        assertThrows(InvalidDateException.class, () -> rewardService.addTransaction(futureTransaction));
    }

    @Test
    void testCalculateRewardPoints() {
        assertEquals(90, rewardService.calculateRewardPoints(transaction1)); // 120 -> 2 * 20 = 40 + 50*1=90
        assertEquals(30, rewardService.calculateRewardPoints(transaction2)); // 80 -> 30 points
    }

    @Test
    void testCalculateRewardsForMonth() {
        when(rewardRepo.existsByCustomerId("C123")).thenReturn(true);
        when(rewardRepo.findAllByCustomerId("C123")).thenReturn(transactions);
        
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        CustomerRewardsSummary result = rewardService.calculateRewardsForMonth("C123", currentMonth, currentYear);
        
        assertNotNull(result);
        assertEquals("C123", result.getCustomerId());
        assertEquals(120, result.getTotalPoints());
    }

    @Test
    void testCalculateRewardsForMonth_NoTransactionsFound() {
        when(rewardRepo.existsByCustomerId("C123")).thenReturn(true);
        when(rewardRepo.findAllByCustomerId("C123")).thenReturn(Collections.emptyList());

        assertThrows(TransactionNotFoundException.class, () -> rewardService.calculateRewardsForMonth("C123", LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
    }

    @Test
    void testCalculateRewardsForMonth_InvalidDate() {
        when(rewardRepo.existsByCustomerId("C123")).thenReturn(true);
        when(rewardRepo.findAllByCustomerId("C123")).thenReturn(Arrays.asList(transaction3));

        assertThrows(InvalidDateException.class, () -> rewardService.calculateRewardsForMonth("C123", transaction3.getTransactionDate().getMonthValue(), transaction3.getTransactionDate().getYear()));
    }

    @Test
    void testCalculateRewardsForPreviousThreeMonths() {
        when(rewardRepo.existsByCustomerId("C123")).thenReturn(true);
        when(rewardRepo.findAllByCustomerId("C123")).thenReturn(transactions);

        CustomerRewardsSummary result = rewardService.calculateRewardsForPreviousThreeMonths("C123");
        
        assertNotNull(result);
        assertEquals("C123", result.getCustomerId());
        assertEquals(120, result.getTotalPoints());
    }

    @Test
    void testCalculateRewardsForPreviousThreeMonths_NoTransactions() {
        when(rewardRepo.existsByCustomerId("C125")).thenReturn(true);
        when(rewardRepo.findAllByCustomerId("C125")).thenReturn(Collections.emptyList());

        assertThrows(TransactionNotFoundException.class, () -> rewardService.calculateRewardsForPreviousThreeMonths("C125"));
    }
}

