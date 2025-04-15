package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.DoubleBuffer;

@Component
public class HandlerType3 implements TaqMessageHandler {

    private final EmaPublisher publisher;

    @Autowired
    public HandlerType3(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(String[] fields) {
        String ric = fields[2];
        long size = Long.parseLong(fields[7]);
        double price = Double.parseDouble(fields[8]);
        publisher.publishTrade(ric,price,size);
    }
}
