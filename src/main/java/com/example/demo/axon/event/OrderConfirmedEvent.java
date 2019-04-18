package com.example.demo.axon.event;

import lombok.Data;

@Data
public class OrderConfirmedEvent {
    private final String orderId;
}