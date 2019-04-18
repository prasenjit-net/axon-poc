package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class CreditAccountCommand {
	@TargetAggregateIdentifier
	private String accountId;
	private int amount;
}
