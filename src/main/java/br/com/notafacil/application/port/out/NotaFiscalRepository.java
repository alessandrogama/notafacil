package br.com.notafacil.application.port.out;
import br.com.notafacil.domain.model.NotaFiscal;
import java.util.Optional;
import java.util.UUID;
public interface NotaFiscalRepository {

    NotaFiscal salvar(NotaFiscal notaFiscal);

    Optional<NotaFiscal> buscarPorId(UUID id);
}
