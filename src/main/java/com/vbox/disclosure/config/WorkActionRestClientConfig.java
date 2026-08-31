package com.vbox.disclosure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
@EnableConfigurationProperties(WorkActionClientProperties.class)
public class WorkActionRestClientConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean("workActionRestClient")
    RestClient workActionRestClient(RestClient.Builder restClientBuilder, WorkActionClientProperties properties) {
        log.info("Configuring Work Action RestClient baseUrl={}", properties.baseUrl());
        return restClientBuilder
                .baseUrl(properties.baseUrl())
                .build();
    }
}
