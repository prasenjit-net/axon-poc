package com.example.demo.axon.query;

import lombok.Data;

@Data
public class OrderDetailsQuery {
	private final String orderId;
	
	@Data
	public class Result{
		private String orderId;
		private String accountId;
		private int amount;
		private boolean confirmed;
		private boolean shipped;
	}
}
