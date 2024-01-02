package org.justlife.booking.model.base;

import org.apache.logging.log4j.message.FormattedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BusinessException extends ResponseStatusException {

    public BusinessException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BusinessException(HttpStatus status, String message) {
        super(status, message);
    }

    public BusinessException(String message, Object... args) {
        super(HttpStatus.BAD_REQUEST, new FormattedMessage(message, args).toString());
    }
}
