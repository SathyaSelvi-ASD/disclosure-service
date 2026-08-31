package com.vbox.disclosure.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageResolver {
    private final MessageSource messageSource;
    public String get(MessageKey key, Locale locale, Object... args) {
        log.info("Resolving message key={} locale={}", key.key(), locale);
        return messageSource.getMessage(key.key(), args, locale);
    }
}
