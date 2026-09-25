package com.vbox.disclosure.application;

import org.springframework.stereotype.Component;

@Component
public class DefaultWorkActionGateway implements WorkActionGateway {
    @Override
    public boolean exists(String workActionId) {
        return workActionId != null && !workActionId.isBlank();
    }
}
