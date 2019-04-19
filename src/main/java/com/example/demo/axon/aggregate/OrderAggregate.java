package com.example.demo.axon.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jboss.aerogear.security.otp.api.Base32;

import com.example.demo.axon.command.ConfirmOrderCommand;
import com.example.demo.axon.command.PlaceOrderCommand;
import com.example.demo.axon.command.ShipOrderCommand;
import com.example.demo.axon.event.OrderConfirmedEvent;
import com.example.demo.axon.event.OrderPlacedEvent;
import com.example.demo.axon.event.OrderShippedEvent;

import lombok.Data;

@Data
@Aggregate
public class OrderAggregate {

	public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

	@AggregateIdentifier
	private String orderId;
	private int amount;
	private boolean orderConfirmed;
	private boolean orderShipped;

	@CommandHandler
	public OrderAggregate(PlaceOrderCommand command) {
		AggregateLifecycle.apply(new OrderPlacedEvent(command.getOrderId(), command.getProduct(), Base32.random()));
	}

	@EventSourcingHandler
	public void on(OrderPlacedEvent event) {
		this.orderId = event.getOrderId();
		orderConfirmed = false;
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

	protected OrderAggregate() {
	}
}