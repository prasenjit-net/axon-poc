package com.example.demo.axon.event;

import lombok.Data;

@Data
public class CreditAccountEvent {
	private final String accountId;
	private final int amount;
}
