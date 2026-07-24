package br.com.notafacil.infrastructure.adapter.out.persistence;

import br.com.notafacil.application.port.out.NotaFiscalRepository;
import br.com.notafacil.domain.model.Cnpj;
import br.com.notafacil.domain.model.ItemNota;
import br.com.notafacil.domain.model.NotaFiscal;
import br.com.notafacil.domain.model.StatusNota;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotaFiscalRepositoryAdapter implements NotaFiscalRepository {

    private final NotaFiscalJpaRepository jpaRepository;

    public NotaFiscalRepositoryAdapter(NotaFiscalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public NotaFiscal salvar(NotaFiscal notaFiscal) {
        var entity = paraEntity(notaFiscal);
        var salva = jpaRepository.save(entity);
        return paraDominio(salva);
    }

    @Override
    public Optional<NotaFiscal> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::paraDominio);
    }

    private NotaFiscalJpaEntity paraEntity(NotaFiscal nota) {
        var entity = NotaFiscalJpaEntity.builder()
                .id(nota.getId())
                .cnpjEmitente(nota.getEmitente().valor())
                .cnpjDestinatario(nota.getDestinatario().valor())
                .status(nota.getStatus().name())
                .chaveAcesso(nota.getChaveAcesso())
                .protocoloAutorizacao(nota.getProtocoloAutorizacao())
                .criadaEm(nota.getCriadaEm())
                .autorizadaEm(nota.getAutorizadaEm())
                .motivoRejeicao(nota.getMotivoRejeicao())
                .build();

        var itens = nota.getItens().stream()
                .map(item -> new ItemNotaJpaEntity(null, entity,
                        item.descricao(), item.quantidade(), item.valorUnitario()))
                .toList();
        entity.getItens().addAll(itens);

        return entity;
    }

    private NotaFiscal paraDominio(NotaFiscalJpaEntity entity) {
        List<ItemNota> itens = entity.getItens().stream()
                .map(i -> new ItemNota(i.getDescricao(), i.getQuantidade(), i.getValorUnitario()))
                .toList();

        return NotaFiscal.reconstituir(
                entity.getId(),
                new Cnpj(entity.getCnpjEmitente()),
                new Cnpj(entity.getCnpjDestinatario()),
                itens,
                entity.getCriadaEm(),
                StatusNota.valueOf(entity.getStatus()),
                entity.getChaveAcesso(),
                entity.getProtocoloAutorizacao(),
                entity.getAutorizadaEm(),
                entity.getMotivoRejeicao()
        );
    }
}