package com.di.core;

import com.di.handler.TaqMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TaqDispatcher {
    private final Map<Integer, TaqMessageHandler> handlers = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(TaqDispatcher.class);

    public void register(int msgType, TaqMessageHandler handler) {
        handlers.put(msgType, handler);
    }

    public void dispatch(int msgType, String[] fields) {
        TaqMessageHandler handler = handlers.get(msgType);
        if (handler != null) {
            handler.handle(fields);
        } else {
            LOG.info("Unhandled message type: {}", msgType);
        }
    }
}
