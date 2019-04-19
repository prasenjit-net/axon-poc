package com.example.demo.axon.aggregate;

import com.example.demo.axon.command.acc.CreateAccountCommand;
import com.example.demo.axon.command.acc.DepositCommand;
import com.example.demo.axon.command.acc.PayCommand;
import com.example.demo.axon.event.CreateAccountEvent;
import com.example.demo.axon.event.CreditAccountEvent;
import com.example.demo.axon.event.DebitAccountEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.Repository;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private int balance;

    @Autowired
    private transient AxonConfiguration configuration;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createCommand) {
        AggregateLifecycle.apply(new CreateAccountEvent(createCommand.getAccountId(), createCommand.getAmount()));
    }

    protected AccountAggregate() {
    }

    @EventSourcingHandler
    public void on(CreateAccountEvent caEvent) {
        this.accountId = caEvent.getAccountId();
        this.balance = caEvent.getAmount();
    }

    @CommandHandler
    public void handle(DepositCommand command) {
        AggregateLifecycle.apply(new CreditAccountEvent(command.getAccountId(), command.getAmount()));
    }

    @EventSourcingHandler
    public void on(CreditAccountEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(PayCommand command) {
        String orderId = command.getOrderId();
        Repository<OrderAggregate> orderAggregateRepository = configuration.repository(OrderAggregate.class);
        org.axonframework.modelling.command.Aggregate<OrderAggregate> load = orderAggregateRepository.load(orderId);
        Integer amount = load.invoke(OrderAggregate::getAmount);

        if (amount > balance) {
            throw new IllegalStateException("Not enough balance");
        }

        AggregateLifecycle.apply(new DebitAccountEvent(command.getAccountId(), amount));
    }

    @EventSourcingHandler
    public void on(DebitAccountEvent event) {
        this.balance += event.getAmount();
    }
}
