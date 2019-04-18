package com.example.demo.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class DebitAccountCommand {
	@TargetAggregateIdentifier
	private String accountId;
	private int amount;
}
