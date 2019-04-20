package com.example.demo.axon.query.service;

import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.example.demo.axon.aggregate.OrderAggregate;
import com.example.demo.axon.query.OrderDetailsQuery;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
	private final Repository<OrderAggregate> orderAggregateRepository;

	@QueryHandler
	public OrderDetailsQuery.Result handle(OrderDetailsQuery query) {
		Aggregate<OrderAggregate> order = orderAggregateRepository.load(query.getOrderId());
		OrderDetailsQuery.Result result = query.new Result();
		order.execute(o -> {
			result.setAccountId(o.getAccountId());
			result.setConfirmed(o.isOrderConfirmed());
			result.setShipped(o.isOrderShipped());
			result.setAmount(o.getAmount());
			result.setOrderId(o.getOrderId());
		});
		return result;
	}
}
