package br.com.notafacil.application.exception;

import java.util.UUID;

public class NotaNaoEncontradaException extends RuntimeException {

    public NotaNaoEncontradaException(UUID id) {
        super("Nota fiscal não encontrada: " + id);
    }
}