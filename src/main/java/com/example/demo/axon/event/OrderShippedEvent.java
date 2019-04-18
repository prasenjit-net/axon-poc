package com.example.demo.axon.event;

import lombok.Data;

@Data
public class OrderShippedEvent {
	private final String orderId;
}