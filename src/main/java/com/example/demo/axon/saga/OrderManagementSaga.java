package com.example.demo.axon.saga;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.example.demo.axon.command.AssociateAccountCommand;
import com.example.demo.axon.command.ConfirmOrderCommand;
import com.example.demo.axon.command.CreateAccountCommand;
import com.example.demo.axon.command.PayOrderCommand;
import com.example.demo.axon.event.CreateAccountEvent;
import com.example.demo.axon.event.CreditAccountEvent;
import com.example.demo.axon.event.OrderConfirmedEvent;
import com.example.demo.axon.event.OrderPayedEvent;
import com.example.demo.axon.event.OrderPlacedEvent;
import com.example.demo.axon.query.AccountBalanceQuery;

import lombok.Data;

@Saga
@Data
public class OrderManagementSaga {
	private String orderId;
	private String accountId;
	private boolean paid = false;
	private int amount;

	@Autowired
	private transient CommandGateway commandGateway;
	@Autowired
	private transient QueryGateway queryGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderPlacedEvent event) {
		this.orderId = event.getOrderId();
		this.amount = event.getAmount();
		if (StringUtils.hasText(event.getAccountId())) {
			this.accountId = event.getAccountId();
			SagaLifecycle.associateWith("accountId", accountId);
			int balance = findAccountBalance();
			if (balance > event.getAmount()) {
				PayOrderCommand payOrderCommand = new PayOrderCommand(accountId, orderId, amount);
				commandGateway.send(payOrderCommand);
			}
		} else {
			String accountId = UUID.randomUUID().toString();
			SagaLifecycle.associateWith("accountId", accountId);
			CreateAccountCommand createAccCommand = new CreateAccountCommand(accountId, 0);
			commandGateway.send(createAccCommand);
		}
	}

	private int findAccountBalance() {
		CompletableFuture<Integer> futureBalance = queryGateway.query(new AccountBalanceQuery(this.accountId),
				Integer.class);
		return futureBalance.join();
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderPayedEvent event) {
		this.paid = true;
		ConfirmOrderCommand confirmOrderCommand = new ConfirmOrderCommand(event.getOrderId());
		commandGateway.send(confirmOrderCommand);
		SagaLifecycle.removeAssociationWith("accountId", accountId);
	}

	@SagaEventHandler(associationProperty = "accountId")
	public void handle(CreditAccountEvent event) {
		int balance = findAccountBalance();
		if (balance > amount && !paid) {
			PayOrderCommand payOrderCommand = new PayOrderCommand(accountId, orderId, amount);
			commandGateway.send(payOrderCommand);
		}
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderConfirmedEvent event) {
		SagaLifecycle.end();
	}

	@SagaEventHandler(associationProperty = "accountId")
	public void handle(CreateAccountEvent createAccountEvent) {
		this.accountId = createAccountEvent.getAccountId();
		AssociateAccountCommand associateAccountCommand = new AssociateAccountCommand(orderId,
				createAccountEvent.getAccountId());
		commandGateway.send(associateAccountCommand);
	}
}
