package com.vbox.disclosure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import java.util.Locale;

@Slf4j
@Configuration
public class LocaleResolverConfig {
    @Bean
    LocaleResolver localeResolver() {
        log.info("Configuring Accept-Language locale resolver with English fallback");
        var resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
