package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler112TradeCancel implements TaqMessageHandler {

    private final EmaPublisher publisher;

    private static final Logger LOG = LoggerFactory.getLogger(Handler112TradeCancel.class);
    @Autowired
    public Handler112TradeCancel(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(String[] fields) {
        try {
            String ric = fields[3];
            long cancelSize = Long.parseLong(fields[7]);
            double cancelPrice = Double.parseDouble(fields[8]);

            // Here you may want to send a cancellation notification
            LOG.info("Trade cancel: %s %.2f x %d%n", ric, cancelPrice, cancelSize);
        } catch (Exception e) {
            LOG.error("Failed to handle Trade Cancel (112): " + e.getMessage());
        }
    }
}
