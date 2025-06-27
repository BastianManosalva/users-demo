package com.example.usersdemo.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.usersdemo.exception.ServiceException;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * 
     * @param ServiceException {@link ServiceException}
     * @param WebRequest       {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Object> handleSchemaException(ServiceException ex, WebRequest request) {
        LOGGER.error("Error {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.valueOf(Integer.parseInt(ex.getCode())), ex.getMessage());
    }

    /**
     * 
     * @param status  {@link HttpStatus}
     * @param message {@link String}
     * @return ResponseEntity<Object>
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        ErrorResponse schemaError = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(schemaError, status);
    }

    /**
     * 
     * @param ex {@link MethodArgumentNotValidException}
     * @return ResponseEntity<?>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request");

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please contact support.");
        LOGGER.error("An unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
