package com.interview.ai_interview.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        if (request.getRequestURI().contains("/v3/api-docs")) {
            throw new RuntimeException(message);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    // ===============================
    // Runtime Exception
    // ===============================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ===============================
    // Forbidden
    // ===============================
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleForbidden(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied", request);
    }

    // ===============================
    // File too large
    // ===============================
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleFileSize(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.PAYLOAD_TOO_LARGE, "File too large", request);
    }

    // ===============================
    // All Unhandled Exceptions
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error: " + ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(SttServiceException.class)
    public ResponseEntity<Object> handleStt(
            SttServiceException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_GATEWAY,
                ex.getMessage(),
                request
        );
    }
}
