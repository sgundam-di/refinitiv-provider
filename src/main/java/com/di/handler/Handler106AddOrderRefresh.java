package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler106AddOrderRefresh implements TaqMessageHandler {
    private final EmaPublisher publisher;

    public Handler106AddOrderRefresh(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
        double price = Double.parseDouble(fields[8]);
        long size = Long.parseLong(fields[7]);
        publisher.publishTrade(ric, price, size);
    }
}