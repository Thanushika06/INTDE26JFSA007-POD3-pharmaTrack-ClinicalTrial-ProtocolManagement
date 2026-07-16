package com.cts.pharmaTrack.module.clinicalTrial.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

/**
 * Exception handling scoped to the clinicalTrial controllers.
 * Scoped via basePackages (and given highest precedence) so it does not
 * collide with the app-wide {@code common.exception.GlobalExceptionHandler}.
 * Named distinctly from that class so the two advices do not clash on the
 * default {@code globalExceptionHandler} bean name during component scanning.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.cts.pharmaTrack.module.clinicalTrial.controller")
public class ClinicalTrialExceptionHandler {

    @ExceptionHandler(TrialNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTrialNotFound(
            TrialNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTrialCodeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTrialCode(
            DuplicateTrialCodeException ex) {
        return ResponseEntity
                .status(409)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(ProtocolNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProtocolNotFound(
            ProtocolNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateProtocolVersionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProtocolVersion(
            DuplicateProtocolVersionException ex) {
        return ResponseEntity
                .status(409)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(SiteNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSiteNotFound(
            SiteNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity
                .status(400)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(
            IllegalStateException ex) {
        return ResponseEntity
                .status(400)
                .body(Map.of("message", ex.getMessage()));
    }
}
