package com.example.demo.axon.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.axon.command.CreateAccountCommand;
import com.example.demo.axon.command.DepositCommand;
import com.example.demo.axon.command.PayOrderCommand;
import com.example.demo.axon.event.CreateAccountEvent;
import com.example.demo.axon.event.CreditAccountEvent;
import com.example.demo.axon.event.OrderPayedEvent;

import lombok.Data;

@Data
@Aggregate
public class AccountAggregate {
	@AggregateIdentifier
	private String accountId;
	private int balance;

	@Autowired
	private transient AxonConfiguration configuration;

	@CommandHandler
	public AccountAggregate(CreateAccountCommand createCommand) {
		AggregateLifecycle.apply(new CreateAccountEvent(createCommand.getAccountId(), createCommand.getAmount()));
	}

	protected AccountAggregate() {
	}

	@EventSourcingHandler
	public void on(CreateAccountEvent caEvent) {
		this.accountId = caEvent.getAccountId();
		this.balance = caEvent.getAmount();
	}

	@CommandHandler
	public void handle(DepositCommand command) {
		AggregateLifecycle.apply(new CreditAccountEvent(command.getAccountId(), command.getAmount()));
	}

	@EventSourcingHandler
	public void on(CreditAccountEvent event) {
		this.balance += event.getAmount();
	}

	@CommandHandler
	public void handle(PayOrderCommand command) {
		int orderAmount = command.getAmount();
		if (this.balance < orderAmount) {
			throw new IllegalStateException("Not enough balance");
		}
		AggregateLifecycle.apply(new OrderPayedEvent(command.getAccountId(), command.getOrderId(), orderAmount));
	}

	@EventSourcingHandler
	public void on(OrderPayedEvent event) {
		this.balance -= event.getAmount();
	}
}
