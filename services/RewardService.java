
package com.home.work.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.home.work.Entity.Transaction;
import com.home.work.exception.CustomerNotFoundException;
import com.home.work.exception.InvalidDateException;
import com.home.work.exception.TransactionNotFoundException;
import com.home.work.model.CustomerRewardsSummary;
import com.home.work.repo.RewardRepo;

@Service
public class RewardService {
	
	@Autowired
	private RewardRepo rewardRepo;
	

//    private static List<Transaction> transactions = new ArrayList<>();

    // Method to add a transaction dynamically
    public void addTransaction(Transaction transaction) {
    	
    	
            LocalDate currentDate = LocalDate.now();
            LocalDate transactionDate = transaction.getTransactionDate();
            
            if (transactionDate.isAfter(currentDate)) {
                throw new InvalidDateException("Transaction date cannot be in the future.");
            }

            // Restrict transactions older than 3 months from current month
            if (transactionDate.isBefore(currentDate.minusMonths(3))) {
                throw new InvalidDateException("Transactions older than 3 months are not allowed.");
            }

            rewardRepo.save(transaction);
        
}
    
	
    public int calculateRewardPoints(Transaction transaction) {
        double amount = transaction.getAmount();
        int points = 0;

  
        if (amount > 100) {
            points += (int) ((amount - 100) * 2); 
            amount = 100; 
        }

        
        if (amount > 50) {
            points += (int) ((amount - 50) * 1);
        }

        return points;
    }
    
    
    public CustomerRewardsSummary calculateRewardsForMonth(String customerId,int month,int year) {
    	
    	LocalDate currentDate = LocalDate.now();

        // Validate if customer ID exists
        if (!rewardRepo.existsByCustomerId(customerId)) {
            throw new CustomerNotFoundException("Customer ID not found in the database.");
        }

        // Restrict transactions older than 3 months from current month
        LocalDate inputDate = LocalDate.of(year, month, 1);
        if (inputDate.isBefore(currentDate.minusMonths(3))) {
            throw new InvalidDateException("Transactions older than 3 months are not allowed.");
        }

           Map<Month, Integer> monthlyPoints = new HashMap<>();
           int totalPoints = 0;
           List<Transaction> transactions=rewardRepo.findAllByCustomerId(customerId);
           
         for (Transaction transaction : transactions) {
            if (
                transaction.getTransactionDate().getMonthValue() == month &&
                transaction.getTransactionDate().getYear() == year) {

            int points = calculateRewardPoints(transaction);
            totalPoints += points;

            Month transactionMonth = transaction.getTransactionDate().getMonth();
            monthlyPoints.put(transactionMonth, monthlyPoints.getOrDefault(transactionMonth, 0) + points);
            
            
        }
           
       }
         if (totalPoints == 0) {
             throw new TransactionNotFoundException("No transactions found for the given month and year.");
         } 
    return new CustomerRewardsSummary(customerId, monthlyPoints, totalPoints);
  }
    
    
    
    
    public CustomerRewardsSummary calculateRewardsForPreviousThreeMonths(
            @PathVariable String customerId) {

        LocalDate currentDate = LocalDate.now();
        
        if (!rewardRepo.existsByCustomerId(customerId)) {
            throw new CustomerNotFoundException("Customer ID not found in the database.");
        }
        
        Map<Month, Integer> monthlyPoints = new HashMap<>();
        int totalPoints = 0;
        List<Transaction> transactions=rewardRepo.findAllByCustomerId(customerId);
        // Iterate over last three months
        for (int i = 0; i < 3; i++) {
            LocalDate startOfMonth = currentDate.minusMonths(i).withDayOfMonth(1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            for (Transaction transaction : transactions) {
                if (transaction.getCustomerId().equals(customerId) &&
                        !transaction.getTransactionDate().isBefore(startOfMonth) &&
                        !transaction.getTransactionDate().isAfter(endOfMonth)) {

                    int points = calculateRewardPoints(transaction);
                    totalPoints += points;

                    Month transactionMonth = transaction.getTransactionDate().getMonth();
                    monthlyPoints.put(transactionMonth, monthlyPoints.getOrDefault(transactionMonth, 0) + points);
                }
            }
            
            if (totalPoints == 0) {
                throw new TransactionNotFoundException("No transactions found in the previous three months.");
            }
        }

        return new CustomerRewardsSummary(customerId, monthlyPoints, totalPoints);
    }
    
}