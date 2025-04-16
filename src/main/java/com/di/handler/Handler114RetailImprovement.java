package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler114RetailImprovement implements TaqMessageHandler {

    private final EmaPublisher publisher;

    @Autowired
    public Handler114RetailImprovement(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(String[] fields) {
        String ric = fields[3];
        double price = Double.parseDouble(fields[8]);
        long size = Long.parseLong(fields[7]);
        publisher.publishTrade(ric, price, size);
    }
}