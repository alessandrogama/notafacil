package br.com.notafacil.application.usecase;

import br.com.notafacil.application.exception.NotaNaoEncontradaException;
import br.com.notafacil.application.port.in.ConsultarNotaUseCase;
import br.com.notafacil.application.port.out.NotaFiscalRepository;
import br.com.notafacil.domain.model.NotaFiscal;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsultarNotaUseCaseImpl implements ConsultarNotaUseCase {

    private final NotaFiscalRepository repository;

    public ConsultarNotaUseCaseImpl(NotaFiscalRepository repository) {
        this.repository = repository;
    }

    @Override
    public NotaFiscal consultarPorId(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NotaNaoEncontradaException(id));
    }
}