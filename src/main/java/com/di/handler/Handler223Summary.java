package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler223Summary implements TaqMessageHandler {

    private final EmaPublisher publisher;

    private static final Logger LOG = LoggerFactory.getLogger(Handler223Summary.class);
    @Autowired
    public Handler223Summary(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(String[] fields) {
        try {
            String ric = fields[3];
            double high = Double.parseDouble(fields[8]);
            double low = Double.parseDouble(fields[9]);
            double close = Double.parseDouble(fields[10]);

            publisher.publishTrade(ric, high,123);
            System.out.printf("Summary for %s: High %.2f | Low %.2f | Close %.2f%n", ric, high, low, close);
            // Optionally, send as MarketPrice or SummaryMsg
        } catch (Exception e) {
            System.err.println("Failed to handle Summary (223): " + e.getMessage());
        }
    }
}
