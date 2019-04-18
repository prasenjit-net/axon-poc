package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class PlaceOrderCommand {
	@TargetAggregateIdentifier
	private final String orderId;
	private final String product;
	private final String accountId;
}