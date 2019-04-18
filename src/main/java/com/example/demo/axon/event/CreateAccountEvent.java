package com.example.demo.axon.event;

import lombok.Data;

@Data
public class CreateAccountEvent {
	private final String accountId;
	private final int amount;
}
