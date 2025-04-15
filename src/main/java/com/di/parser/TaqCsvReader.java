package com.di.parser;


import com.di.config.AppConfig;
import com.di.core.TaqDispatcher;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@Component
public class TaqCsvReader {

    private final AppConfig config;
    private final TaqDispatcher dispatcher;

    public TaqCsvReader(AppConfig config, TaqDispatcher dispatcher) {
        this.config = config;
        this.dispatcher = dispatcher;
    }
    @PostConstruct
    public void init() {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new GZIPInputStream(new FileInputStream(config.getGzipFile())), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                int msgType = Integer.parseInt(fields[0].trim());
                dispatcher.dispatch(msgType, fields);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse TAQ file: " + config.getGzipFile(), e);
        }
    }
}
