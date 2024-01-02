package org.justlife.booking.config;

import jakarta.validation.constraints.NotNull;
import org.justlife.booking.model.base.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        // casting the generic Exception e to CustomErrorException
        BusinessException customErrorException = new BusinessException(HttpStatus.valueOf(500), e.getMessage());

        HttpStatus status = HttpStatus.resolve(customErrorException.getStatusCode().value());

        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        customErrorException.printStackTrace(printWriter);

        return new ResponseEntity<>(getErrorResponse(customErrorException), status);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(Exception e) {
        // casting the generic Exception e to CustomErrorException
        BusinessException customErrorException = (BusinessException) e;

        HttpStatus status = HttpStatus.resolve(customErrorException.getStatusCode().value());

        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        customErrorException.printStackTrace(printWriter);

        return new ResponseEntity<>(getErrorResponse(customErrorException), status);
    }

    private static ErrorResponse getErrorResponse(BusinessException customErrorException) {
        return new ErrorResponse() {
            @Override
            public @NotNull HttpStatusCode getStatusCode() {
                return customErrorException.getStatusCode();
            }

            @Override
            public @NotNull ProblemDetail getBody() {
                return customErrorException.getBody();
            }
        };
    }

}
