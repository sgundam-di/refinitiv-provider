package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler112TradeCancel implements TaqMessageHandler {

    private final EmaPublisher publisher;

    @Autowired
    public Handler112TradeCancel(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(String[] fields) {
        try {
            String ric = fields[2];
            long cancelSize = Long.parseLong(fields[7]);
            double cancelPrice = Double.parseDouble(fields[8]);

            // Here you may want to send a cancellation notification
            System.out.printf("Trade cancel: %s %.2f x %d%n", ric, cancelPrice, cancelSize);
        } catch (Exception e) {
            System.err.println("Failed to handle Trade Cancel (112): " + e.getMessage());
        }
    }
}
