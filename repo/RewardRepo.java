package com.home.work.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.home.work.Entity.Transaction;

@Repository
public interface RewardRepo extends JpaRepository<Transaction, Integer> {

	List<Transaction> findAllByCustomerId(String customerId);

	boolean existsByCustomerId(String customerId);

	
	

}
