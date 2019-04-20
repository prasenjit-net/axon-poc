package com.example.demo.axon.event;

import lombok.Data;

@Data
public class AccountAssociatedEvent {
	private final String orderId;
	private final String accountId;
}
