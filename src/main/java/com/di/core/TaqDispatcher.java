package com.di.core;

import com.di.handler.TaqMessageHandler;

import java.util.HashMap;
import java.util.Map;

public class TaqDispatcher {
    private final Map<Integer, TaqMessageHandler> handlers = new HashMap<>();

    public void register(int msgType, TaqMessageHandler handler) {
        handlers.put(msgType, handler);
    }

    public void dispatch(int msgType, String[] fields) {
        TaqMessageHandler handler = handlers.get(msgType);
        if (handler != null) {
            handler.handle(fields);
        } else {
            System.out.println("Unhandled message type: " + msgType);
        }
    }
}
