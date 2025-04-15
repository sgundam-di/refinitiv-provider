package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler103OrderExecution implements TaqMessageHandler{

    private final EmaPublisher publisher;

    public Handler103OrderExecution(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    @Override
    public void handle(String[] fields) {
        String ric = fields[3];
        double price = Double.parseDouble(fields[7]);
        long size = Long.parseLong(fields[8]);

        publisher.publishTrade(ric,price,size);

    }
}
