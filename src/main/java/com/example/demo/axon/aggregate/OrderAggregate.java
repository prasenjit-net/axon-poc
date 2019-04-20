package com.example.demo.axon.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.demo.axon.command.AssociateAccountCommand;
import com.example.demo.axon.command.ConfirmOrderCommand;
import com.example.demo.axon.command.PlaceOrderCommand;
import com.example.demo.axon.command.ShipOrderCommand;
import com.example.demo.axon.event.AccountAssociatedEvent;
import com.example.demo.axon.event.OrderConfirmedEvent;
import com.example.demo.axon.event.OrderPlacedEvent;
import com.example.demo.axon.event.OrderShippedEvent;

import lombok.Data;

@Data
@Aggregate
public class OrderAggregate {

	//public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

	@AggregateIdentifier
	private String orderId;
	private String accountId;
	private boolean orderConfirmed;
	private boolean orderShipped;
	private int amount;

	@CommandHandler
	public OrderAggregate(PlaceOrderCommand command) {
		AggregateLifecycle.apply(new OrderPlacedEvent(command.getOrderId(), command.getProduct(),
				command.getAccountId(), command.getPrice()));
	}

	@EventSourcingHandler
	public void on(OrderPlacedEvent event) {
		this.orderId = event.getOrderId();
		this.orderConfirmed = false;
		this.amount = event.getAmount();
		this.accountId = event.getAccountId();
	}

	@CommandHandler
	public void handle(ConfirmOrderCommand command) {
		AggregateLifecycle.apply(new OrderConfirmedEvent(orderId));
	}

	@CommandHandler
	public void handle(ShipOrderCommand command) {
		if (!orderConfirmed) {
			throw new IllegalStateException("Cannot ship an order which has not been confirmed yet.");
		}
		AggregateLifecycle.apply(new OrderShippedEvent(orderId));
	}

	@EventSourcingHandler
	public void on(OrderConfirmedEvent event) {
		orderConfirmed = true;
	}

	@EventSourcingHandler
	public void on(OrderShippedEvent event) {
		orderShipped = true;
	}

	@CommandHandler
	public void handle(AssociateAccountCommand command) {
		AggregateLifecycle.apply(new AccountAssociatedEvent(command.getOrderId(), command.getAccountId()));
	}

	@EventSourcingHandler
	public void on(AccountAssociatedEvent event) {
		this.accountId = event.getAccountId();
	}

	protected OrderAggregate() {
	}
}