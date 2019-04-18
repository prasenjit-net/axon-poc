package com.example.demo.axon.query;

import lombok.Data;

@Data
public class OrderedProduct {
	private final String orderId;
	private final String product;
	private OrderStatus orderStatus = OrderStatus.PLACED;

	public void orderConfirmed() {
		this.orderStatus = OrderStatus.CONFIRMED;
	}

	public void orderShipped() {
		this.orderStatus = OrderStatus.SHIPPED;
	}

	public enum OrderStatus {
		PLACED, CONFIRMED, SHIPPED
	}
}
