package com.ktl.l2store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "paypal")
public class PaypalConfigProperties {

    @ConfigurationProperties(prefix = "client")
    public static class Client {
        String id;
        String secret;
    }

    String mode;
    Client client;
}
