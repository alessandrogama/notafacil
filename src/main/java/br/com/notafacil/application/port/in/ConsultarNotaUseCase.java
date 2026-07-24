package br.com.notafacil.application.port.in;
import br.com.notafacil.domain.model.NotaFiscal;

import java.util.UUID;
public interface ConsultarNotaUseCase {

    NotaFiscal consultarPorId(UUID id);

}
