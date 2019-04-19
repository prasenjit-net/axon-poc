package com.example.demo.axon.command.acc;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class PayCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private String orderId;
}
