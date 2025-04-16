package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler102DeleteOrder implements TaqMessageHandler {
    private final EmaPublisher publisher;

    private static final Logger LOG = LoggerFactory.getLogger(Handler102DeleteOrder.class);
    public Handler102DeleteOrder(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
        LOG.info("Order deleted for RIC: {}", ric);
    }
}