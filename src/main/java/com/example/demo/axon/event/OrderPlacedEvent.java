package com.example.demo.axon.event;

import lombok.Data;

@Data
public class OrderPlacedEvent {
	private final String orderId;
	private final String product;
	private final String accountId;
}