package br.com.notafacil.infrastructure.config;

import br.com.notafacil.application.exception.NotaNaoEncontradaException;
import br.com.notafacil.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento centralizado de exceções: nenhum controller precisa
 * de try/catch — a resposta HTTP é padronizada em um único lugar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(corpo(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ExceptionHandler(NotaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrada(NotaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a, b) -> a));

        var corpo = corpo(HttpStatus.BAD_REQUEST, "Dados inválidos");
        corpo.put("erros", erros);
        return ResponseEntity.badRequest().body(corpo);
    }

    private Map<String, Object> corpo(HttpStatus status, String mensagem) {
        var corpo = new LinkedHashMap<String, Object>();
        corpo.put("timestamp", Instant.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return corpo;
    }
}