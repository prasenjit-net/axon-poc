package com.example.demo.axon;

import com.example.demo.axon.aggregate.OrderAggregate;
import com.example.demo.axon.command.PlaceOrderCommand;
import com.example.demo.axon.command.acc.CreateAccountCommand;
import com.example.demo.axon.event.CreateAccountEvent;
import com.example.demo.axon.event.OrderPlacedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class OrderPaymentTests {
    private FixtureConfiguration<OrderAggregate> orderFixture;

    @Before
    public void setUp() {
        orderFixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    public void testOrderPay() {
        String orderId = UUID.randomUUID().toString();
        String accountId = UUID.randomUUID().toString();

        orderFixture.givenNoPriorActivity()
                .when(new CreateAccountCommand(accountId, 100))
                .expectEvents(new CreateAccountEvent(accountId, 100));

        String product = "Deluxe Chair";
        orderFixture.givenNoPriorActivity().when(new PlaceOrderCommand(orderId, product, accountId, 100))
                .expectEvents(new OrderPlacedEvent(orderId, product, accountId, 100));
    }
}
