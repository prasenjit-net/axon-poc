package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class CreateAccountCommand {
	@TargetAggregateIdentifier
	private final String accountId;
	private final int amount;
}
