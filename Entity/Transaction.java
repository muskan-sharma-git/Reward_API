package com.home.work.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transactionId;
    private String customerId;
    private LocalDate transactionDate;
    private double amount;

    // Constructor, getters, setters
    public Transaction(int transactionId, String customerId, LocalDate transactionDate, double amount) {
        this.transactionId = transactionId; 
    	this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }
    
    public Transaction( String customerId, LocalDate transactionDate, double amount) {
    	this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }
    
    public Transaction() {}

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
    
}