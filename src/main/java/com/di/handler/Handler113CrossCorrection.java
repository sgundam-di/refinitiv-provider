package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler113CrossCorrection implements TaqMessageHandler {
    private final EmaPublisher publisher;

    @Autowired
    public Handler113CrossCorrection(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(String[] fields) {
        String ric = fields[2];
        System.out.println("Cross Correction for: " + ric);
    }
}