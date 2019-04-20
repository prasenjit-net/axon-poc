package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class AssociateAccountCommand {
	@TargetAggregateIdentifier
	private final String orderId;
	private final String accountId;
}
