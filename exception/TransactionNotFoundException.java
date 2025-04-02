package com.home.work.exception;

public class TransactionNotFoundException extends RuntimeException {
	
	public TransactionNotFoundException(String msg) {
        super(msg);
    }
}
