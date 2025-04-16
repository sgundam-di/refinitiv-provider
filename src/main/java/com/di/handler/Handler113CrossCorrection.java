package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler113CrossCorrection implements TaqMessageHandler {
    private final EmaPublisher publisher;

    private static final Logger LOG = LoggerFactory.getLogger(Handler113CrossCorrection.class);

    @Autowired
    public Handler113CrossCorrection(EmaPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(String[] fields) {
        String ric = fields[3];
        LOG.info("Cross Correction for: {}", ric);
    }
}