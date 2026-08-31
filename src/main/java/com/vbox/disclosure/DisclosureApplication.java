package com.vbox.disclosure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DisclosureApplication {
    public static void main(String[] args) {
        log.info("Starting Disclosure Application");
        SpringApplication.run(DisclosureApplication.class, args);
    }
}
