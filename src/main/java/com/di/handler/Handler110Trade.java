package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler110Trade implements TaqMessageHandler{
    private final EmaPublisher publisher;

    public Handler110Trade(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }


    public void handle(String[] fields){
        String ric = fields[3];
        double price = Double.parseDouble(fields[6]);
        long size = Long.parseLong(fields[7]);

        publisher.publishTrade(ric, price, size);
    }
}
