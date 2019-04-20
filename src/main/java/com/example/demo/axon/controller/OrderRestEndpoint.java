package com.example.demo.axon.controller;

import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.axon.command.DepositCommand;
import com.example.demo.axon.command.PlaceOrderCommand;
import com.example.demo.axon.query.FindAllOrderedProductsQuery;
import com.example.demo.axon.query.OrderDetailsQuery;
import com.example.demo.axon.query.OrderDetailsQuery.Result;
import com.example.demo.axon.query.OrderedProduct;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderRestEndpoint {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@GetMapping("/ship-order")
	public String shipOrder() {
		String orderId = UUID.randomUUID().toString();
		Object sendAndWait = commandGateway.sendAndWait(new PlaceOrderCommand(orderId, "Deluxe Chair", "", 100));
		System.out.println(sendAndWait);

		return orderId;
	}

	@GetMapping("/pay-order")
	public Result payOrder(@RequestParam String orderId) {
		Result orderDetails = queryGateway.query(new OrderDetailsQuery(orderId), OrderDetailsQuery.Result.class).join();

		if (!orderDetails.isConfirmed()) {
			String accountId = orderDetails.getAccountId();
			DepositCommand deposit = new DepositCommand(accountId, 50);
			commandGateway.sendAndWait(deposit);
			deposit = new DepositCommand(accountId, 150);
			commandGateway.sendAndWait(deposit);
		}

		return orderDetails;
	}

	@GetMapping("/all-orders")
	public List<OrderedProduct> findAllOrderedProducts() {
		return queryGateway
				.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(OrderedProduct.class))
				.join();
	}
}