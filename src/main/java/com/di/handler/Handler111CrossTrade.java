package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler111CrossTrade implements TaqMessageHandler {
    private final EmaPublisher publisher;

    public Handler111CrossTrade(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
        double price = Double.parseDouble(fields[6]);
        long size = Long.parseLong(fields[5]);
        publisher.publishTrade(ric, price, size);
    }
}