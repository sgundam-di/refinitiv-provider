package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler105Imbalance implements TaqMessageHandler {
    private final EmaPublisher publisher;
    private static final Logger LOG = LoggerFactory.getLogger(Handler105Imbalance.class);

    public Handler105Imbalance(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
        LOG.info("Imbalance for: {} — Value: {}", ric, fields[8]);
    }
}