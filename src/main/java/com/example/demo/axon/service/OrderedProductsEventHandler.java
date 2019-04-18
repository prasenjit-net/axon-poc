package com.example.demo.axon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.example.demo.axon.event.OrderPlacedEvent;
import com.example.demo.axon.query.FindAllOrderedProductsQuery;
import com.example.demo.axon.query.OrderedProduct;

@Service
public class OrderedProductsEventHandler {

	private final Map<String, OrderedProduct> orderedProducts = new HashMap<>();

	@EventHandler
	public void on(OrderPlacedEvent event) {
		String orderId = event.getOrderId();
		orderedProducts.put(orderId, new OrderedProduct(orderId, event.getProduct()));
	}

	// Event Handlers for OrderConfirmedEvent and OrderShippedEvent...

	@QueryHandler
	public List<OrderedProduct> handle(FindAllOrderedProductsQuery query) {
		return new ArrayList<>(orderedProducts.values());
	}
}