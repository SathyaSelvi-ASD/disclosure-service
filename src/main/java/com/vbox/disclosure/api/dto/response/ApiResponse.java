package com.vbox.disclosure.api.dto.response;

import java.util.List;

public record ApiResponse(String status, int statusCode, String message,
                          List<ApiMessage> errors, List<ApiMessage> warnings,
                          Object data) {
}
