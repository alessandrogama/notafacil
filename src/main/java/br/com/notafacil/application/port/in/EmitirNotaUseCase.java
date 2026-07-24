package br.com.notafacil.application.port.in;

import br.com.notafacil.domain.model.NotaFiscal;

public interface EmitirNotaUseCase {

    NotaFiscal emitir(EmitirNotaCommand command);
}