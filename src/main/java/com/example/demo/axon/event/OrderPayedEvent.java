package com.example.demo.axon.event;

import lombok.Data;

@Data
public class OrderPayedEvent {
	private final String accountId;
	private final String orderId;
	private final int amount;
}
