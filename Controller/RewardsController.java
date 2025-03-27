package com.home.work.Controller;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.work.Entity.Transaction;
import com.home.work.Service.RewardService;
import com.home.work.model.CustomerRewardsSummary;


@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

//	    private final RewardService rewardCalculatorService = new RewardService();
	    
	    @Autowired
	    private RewardService rewardService;
	    
	    
	    
	    @PostMapping("/addTransaction/{customerId}/{amount}/{year}/{month}/{day}")
	    public String addTransaction(@PathVariable String customerId,
	                                 @PathVariable double amount,
	                                 @PathVariable int year,
	                                 @PathVariable int month,
	                                 @PathVariable int day) {
	        Transaction transaction = new Transaction(
	             
	            customerId,
	            LocalDate.of(year, month, day),
	            amount
	        );
	        rewardService.addTransaction(transaction);
	        return "Transaction added successfully!";
	    }

	    // Calculate reward points for a specific month
	    @GetMapping("/calculate/{customerId}/{month}/{year}")
	    public CustomerRewardsSummary calculateRewardsForMonth(@PathVariable String customerId, @PathVariable int month,@PathVariable int year) {
            
	     return	rewardService.calculateRewardsForMonth(customerId, month, year);

	    };
	    
	    

	    // Calculate reward points for the previous three months
	    @GetMapping("/calculate/previous-three-months/{customerId}")
	    public CustomerRewardsSummary calculateRewardsForPreviousThreeMonths(@PathVariable String customerId) {

	        return  rewardService.calculateRewardsForPreviousThreeMonths(customerId);
	    }
	    
	}

