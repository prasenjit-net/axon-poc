package com.example.demo.axon.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.demo.axon.command.CreateAccountCommand;
import com.example.demo.axon.command.CreditAccountCommand;
import com.example.demo.axon.command.DebitAccountCommand;
import com.example.demo.axon.event.CreateAccountEvent;
import com.example.demo.axon.event.CreditAccountEvent;
import com.example.demo.axon.event.DebitAccountEvent;

import lombok.Data;

@Data
@Aggregate
public class AccountAggregate {
	@AggregateIdentifier
	private String accountId;
	private int balance;

	@CommandHandler
	public AccountAggregate(CreateAccountCommand createCommand) {
		AggregateLifecycle.apply(new CreateAccountEvent(createCommand.getAccountId(), createCommand.getAmount()));
	}

	@EventSourcingHandler
	public void on(CreateAccountEvent caEvent) {
		this.accountId = caEvent.getAccountId();
		this.balance = caEvent.getAmount();
	}

	@CommandHandler
	public void handle(CreditAccountCommand command) {
		AggregateLifecycle.apply(new CreditAccountEvent(command.getAccountId(), command.getAmount()));
	}

	@EventSourcingHandler
	public void on(CreditAccountEvent event) {
		this.balance += event.getAmount();
	}

	@CommandHandler
	public void handle(DebitAccountCommand command) {
		if (this.balance < command.getAmount()) {
			throw new IllegalStateException("Not enough balance");
		}
		AggregateLifecycle.apply(new DebitAccountEvent(command.getAccountId(), command.getAmount()));
	}

	@EventSourcingHandler
	public void on(DebitAccountEvent event) {
		this.balance += event.getAmount();
	}
}
