package com.rud3xl.sbp.exception.handler;


import com.rud3xl.sbp.dto.ApiErrorResponse;
import com.rud3xl.sbp.exception.OrganizationExistsException;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiErrorResponse buildResponse(HttpStatus status, String message, List<String> details, String path) {
        String traceId = UUID.randomUUID().toString();

        if (status.is5xxServerError()) {
            log.error("Error {}: {} at path {}", traceId, message, path);
        } else {
            log.warn("Warning {}: {} at path {}", traceId, message, path);
        }

        return ApiErrorResponse.builder()
                .status(status.value())
                .code(status.name())
                .message(message)
                .details(details != null ? details : new ArrayList<>())
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(NoResourceFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ApiErrorResponse error = buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                details,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);

        ApiErrorResponse error = buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(Exception ex, HttpServletRequest request) {
        log.error("Bad credentials", ex);
        ApiErrorResponse error = buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwt(Exception ex, HttpServletRequest request) {
        log.error("Expired jwt", ex);
        ApiErrorResponse error = buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Your session has expired, please log in again.",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OrganizationExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleOrganizationExists(OrganizationExistsException ex, HttpServletRequest request) {
        ApiErrorResponse error = buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
