package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class PayOrderCommand {
	@TargetAggregateIdentifier
	private final String accountId;
	private final String orderId;
	private final int amount;
}
