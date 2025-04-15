package com.di.handler;


import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler100AddOrder implements TaqMessageHandler {

    private final EmaPublisher publisher;

    @Autowired
    public Handler100AddOrder(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(String[] fields) {
        try {
            String ric = fields[3];
            long size = Long.parseLong(fields[7]);
            double price = Double.parseDouble(fields[6]);

            publisher.publishTrade(ric, price, size); // Reuse trade format for now
        } catch (Exception e) {
            System.err.println("Failed to handle Add Order (100): " + e.getMessage());
        }
    }
}
