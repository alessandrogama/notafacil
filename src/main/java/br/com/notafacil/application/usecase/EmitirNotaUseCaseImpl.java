package br.com.notafacil.application.usecase;

import br.com.notafacil.application.port.in.EmitirNotaCommand;
import br.com.notafacil.application.port.in.EmitirNotaUseCase;
import br.com.notafacil.application.port.out.NotaFiscalRepository;
import br.com.notafacil.domain.model.Cnpj;
import br.com.notafacil.domain.model.ItemNota;
import br.com.notafacil.domain.model.NotaFiscal;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class EmitirNotaUseCaseImpl implements EmitirNotaUseCase {
    private final NotaFiscalRepository repository;

    public EmitirNotaUseCaseImpl(NotaFiscalRepository repository) {
        this.repository = repository;
    }
    @Override
    public NotaFiscal emitir(EmitirNotaCommand command) {
        var emitente = new Cnpj(command.cnpjEmitente());
        var destinatario = new Cnpj(command.cnpjDestinatario());
        List<ItemNota> itens = command.itens().stream()
                .map(i -> new ItemNota(i.descricao(), i.quantidade(), i.valorUnitario()))
                .toList();

        var nota = NotaFiscal.criarRascunho(emitente, destinatario, itens);
        nota.validar();

        return repository.salvar(nota);
    }
}
