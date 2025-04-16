package com.di.handler;


import com.di.core.TaqDispatcher;
import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler100AddOrder implements TaqMessageHandler {

    private final EmaPublisher publisher;
    private static final Logger LOG = LoggerFactory.getLogger(Handler100AddOrder.class);

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
            LOG.error("Failed to handle Add Order (100): {}", e.getMessage());
        }
    }
}
