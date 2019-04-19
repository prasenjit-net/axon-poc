package com.example.demo.axon.command.acc;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class DepositCommand {
	@TargetAggregateIdentifier
	private String accountId;
	private int amount;
}
