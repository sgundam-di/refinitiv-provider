package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.stereotype.Component;

@Component
public class HandlerType34 implements TaqMessageHandler {

    private final EmaPublisher emaPublisher;

    public HandlerType34(EmaPublisher emaPublisher)
    {
        this.emaPublisher = emaPublisher;
    }

    public void handle(String[] fields) {

       // System.out.println("Admin Message Type 34: " + String.join(",", fields));
    }
}