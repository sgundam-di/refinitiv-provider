package com.di.handler;

import com.di.publisher.EmaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Handler102DeleteOrder implements TaqMessageHandler {
    private final EmaPublisher publisher;

    public Handler102DeleteOrder(EmaPublisher emaPublisher){
        this.publisher = emaPublisher;
    }
    public void handle(String[] fields) {
        String ric = fields[3];
       // System.out.println("Order deleted for RIC: " + ric);
    }
}