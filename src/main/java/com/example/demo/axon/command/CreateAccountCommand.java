package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class CreateAccountCommand {
	@TargetAggregateIdentifier
	private String accountId;
	private int amount;
}
