package com.diogo.controleFinanceiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(Map.of("erro", "Dados inválidos", "campos", campos));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> regraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> credenciais(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", ex.getMessage()));
    }
}
