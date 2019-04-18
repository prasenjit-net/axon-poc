package com.example.demo.axon.controller;

import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.axon.aggregate.OrderAggregate;
import com.example.demo.axon.command.ConfirmOrderCommand;
import com.example.demo.axon.command.PlaceOrderCommand;
import com.example.demo.axon.command.ShipOrderCommand;
import com.example.demo.axon.query.FindAllOrderedProductsQuery;
import com.example.demo.axon.query.OrderedProduct;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderRestEndpoint {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	private final AxonConfiguration configuration;

	@GetMapping("/ship-order")
	@Transactional
	public void shipOrder() {
		String orderId = UUID.randomUUID().toString();
		commandGateway.send(new PlaceOrderCommand(orderId, "Deluxe Chair", ""));//TODO

		DefaultUnitOfWork<Message<?>> startAndGet = DefaultUnitOfWork.startAndGet(null);
		Aggregate<OrderAggregate> load = configuration.repository(OrderAggregate.class).load(orderId);
		load.execute(a -> System.out.println(a.getOrderId()));
		startAndGet.rollback();

		commandGateway.send(new ConfirmOrderCommand(orderId));
		commandGateway.send(new ShipOrderCommand(orderId));
	}

	@GetMapping("/all-orders")
	public List<OrderedProduct> findAllOrderedProducts() {
		return queryGateway
				.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(OrderedProduct.class))
				.join();
	}
}