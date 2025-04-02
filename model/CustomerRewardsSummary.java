package com.home.work.model;
import java.time.Month;
import java.util.Map;

public class CustomerRewardsSummary {
    private String customerId;
    private Map<Month, Integer> monthlyPoints;
    private int totalPoints;

    public CustomerRewardsSummary(String customerId, Map<Month, Integer> monthlyPoints, int totalPoints) {
        this.customerId = customerId;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<Month, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
