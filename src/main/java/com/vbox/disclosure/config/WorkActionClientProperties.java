package com.vbox.disclosure.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "work-action.client")
public record WorkActionClientProperties(
        @NotBlank String baseUrl,
        @NotBlank
        @Pattern(regexp = "^/.*", message = "searchPath must start with '/'")
        String searchPath
) {
}
