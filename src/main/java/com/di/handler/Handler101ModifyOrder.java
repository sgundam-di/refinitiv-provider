package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler101ModifyOrder implements TaqMessageHandler {
    private final EmaPublisher publisher;

    public Handler101ModifyOrder(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
        double newPrice = Double.parseDouble(fields[6]);
        long newSize = Long.parseLong(fields[7]);
        publisher.publishTrade(ric, newPrice, newSize);
    }
}