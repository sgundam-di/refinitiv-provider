package com.di.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "taq")
@Configuration
public class AppConfig {
    @Value("${taq.gzipFile}")
    private String gzipFile;

    @Value("${taq.serviceName}")
    private String serviceName;

    public String getGzipFile() {
        return gzipFile;
    }

    public void setGzipFile(String gzipFile) {
        this.gzipFile = gzipFile;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
