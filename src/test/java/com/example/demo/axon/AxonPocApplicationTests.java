package com.example.demo.axon;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;

import com.example.demo.axon.aggregate.OrderAggregate;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AxonPocApplicationTests {
	private FixtureConfiguration<OrderAggregate> fixture;

	@Before
	public void setUp() {
		fixture = new AggregateTestFixture<>(OrderAggregate.class);
	}

//	@Test
//	public void orderPlacesTest() {
//		String orderId = UUID.randomUUID().toString();
//		String product = "Deluxe Chair";
//		fixture.givenNoPriorActivity().when(new PlaceOrderCommand(orderId, product))
//				.expectEvents(new OrderPlacedEvent(orderId, product));
//	}
//
//	@Test
//	public void orderShippedErrorTest() {
//		String orderId = UUID.randomUUID().toString();
//		String product = "Deluxe Chair";
//		fixture.given(new OrderPlacedEvent(orderId, product)).when(new ShipOrderCommand(orderId))
//				.expectException(IllegalStateException.class);
//	}
//
//	@Test
//	public void orderShippedTest() {
//		String orderId = UUID.randomUUID().toString();
//		String product = "Deluxe Chair";
//		fixture.given(new OrderPlacedEvent(orderId, product), new OrderConfirmedEvent(orderId))
//				.when(new ShipOrderCommand(orderId)).expectEvents(new OrderShippedEvent(orderId));
//	}
}
