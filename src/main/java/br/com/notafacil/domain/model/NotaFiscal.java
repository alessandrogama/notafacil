package br.com.notafacil.domain.model;
import br.com.notafacil.domain.exception.DomainException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class NotaFiscal {

    private static final Duration JANELA_CANCELAMENTO = Duration.ofHours(24);

    private final UUID id;
    private final Cnpj emitente;
    private final Cnpj destinatario;
    private final List<ItemNota> itens;
    private final Instant criadaEm;

    private StatusNota status;
    private String chaveAcesso;
    private String protocoloAutorizacao;
    private Instant autorizadaEm;
    private String motivoRejeicao;

    private NotaFiscal(UUID id, Cnpj emitente, Cnpj destinatario, List<ItemNota> itens, Instant criadaEm) {
        this.id = id;
        this.emitente = emitente;
        this.destinatario = destinatario;
        this.itens = List.copyOf(itens);
        this.criadaEm = criadaEm;
        this.status = StatusNota.RASCUNHO;
    }

    public static NotaFiscal criarRascunho(Cnpj emitente, Cnpj destinatario, List<ItemNota> itens) {
        if (emitente == null || destinatario == null) {
            throw new DomainException("Nota exige emitente e destinatário");
        }
        if (emitente.equals(destinatario)) {
            throw new DomainException("Emitente e destinatário devem ser diferentes");
        }
        if (itens == null || itens.isEmpty()) {
            throw new DomainException("Nota deve ter ao menos um item");
        }
        return new NotaFiscal(UUID.randomUUID(), emitente, destinatario, itens, Instant.now());
    }

    // Notas - Transições de estado

    public void validar() {
        transicionarPara(StatusNota.VALIDADA);
    }

    public void enfileirar() {
        transicionarPara(StatusNota.ENFILEIRADA);
    }

    public void iniciarProcessamento() {
        transicionarPara(StatusNota.EM_PROCESSAMENTO);
    }

    public void autorizar(String chaveAcesso, String protocolo, Instant quando) {
        if (chaveAcesso == null || chaveAcesso.length() != 44) {
            throw new DomainException("Chave de acesso deve ter 44 dígitos");
        }
        transicionarPara(StatusNota.AUTORIZADA);
        this.chaveAcesso = chaveAcesso;
        this.protocoloAutorizacao = protocolo;
        this.autorizadaEm = quando;
    }
    public void rejeitar(String motivo) {
        transicionarPara(StatusNota.REJEITADA);
        this.motivoRejeicao = motivo;
    }

    public void entrarEmContingencia() {
        transicionarPara(StatusNota.EM_CONTINGENCIA);
    }
    public void cancelar(Instant quando) {
        if (status == StatusNota.AUTORIZADA
                && Duration.between(autorizadaEm, quando).compareTo(JANELA_CANCELAMENTO) > 0) {
            throw new DomainException(
                    "Cancelamento fora da janela de %d horas".formatted(JANELA_CANCELAMENTO.toHours()));
        }
        transicionarPara(StatusNota.CANCELADA);
    }
    private void transicionarPara(StatusNota destino) {
        if (!status.podeTransicionarPara(destino)) {
            throw new DomainException(
                    "Transição inválida: %s -> %s".formatted(status, destino));
        }
        this.status = destino;
    }

    //Notas - Consultas

    public BigDecimal valorTotal() {
        return itens.stream()
                .map(ItemNota::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() { return id; }
    public Cnpj getEmitente() { return emitente; }
    public Cnpj getDestinatario() { return destinatario; }
    public List<ItemNota> getItens() { return itens; }
    public Instant getCriadaEm() { return criadaEm; }
    public StatusNota getStatus() { return status; }
    public String getChaveAcesso() { return chaveAcesso; }
    public String getProtocoloAutorizacao() { return protocoloAutorizacao; }
    public Instant getAutorizadaEm() { return autorizadaEm; }
    public String getMotivoRejeicao() { return motivoRejeicao; }

}
