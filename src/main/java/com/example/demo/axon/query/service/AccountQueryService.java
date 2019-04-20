package com.example.demo.axon.query.service;

import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.example.demo.axon.aggregate.AccountAggregate;
import com.example.demo.axon.query.AccountBalanceQuery;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountQueryService {
	private final Repository<AccountAggregate> accountAggregateRepository;

	@QueryHandler
	public int handle(AccountBalanceQuery query) {
		Aggregate<AccountAggregate> account = accountAggregateRepository.load(query.getAccountId());
		return account.invoke(AccountAggregate::getBalance);
	}
}
