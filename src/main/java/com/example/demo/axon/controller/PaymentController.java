package com.example.demo.axon.controller;

import com.example.demo.axon.model.PayModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final CommandGateway commandGateway;

    @PostMapping("pay")
    public void pay(@RequestBody PayModel payModel) {

    }
}
