package br.com.notafacil.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import br.com.notafacil.domain.exception.DomainException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotaFiscalTest {
    private static final Cnpj EMITENTE = new Cnpj("11222333000181");
    private static final Cnpj DESTINATARIO = new Cnpj("11444777000161");
    private static final String CHAVE_VALIDA = "3".repeat(44);

    private NotaFiscal novaNota() {
        return NotaFiscal.criarRascunho(EMITENTE, DESTINATARIO,
                List.of(new ItemNota("Serviço de manutenção de TI", 2, new BigDecimal("237.75"))));
    }

    private NotaFiscal notaEmProcessamento() {
        var nota = novaNota();
        nota.validar();
        nota.enfileirar();
        nota.iniciarProcessamento();
        return nota;
    }
    @Nested
    class Criacao {

        @Test
        void nasceComoRascunhoComIdEValorTotal() {
            var nota = novaNota();
            assertThat(nota.getStatus()).isEqualTo(StatusNota.RASCUNHO);
            assertThat(nota.getId()).isNotNull();
            assertThat(nota.valorTotal()).isEqualByComparingTo("475.50");
        }

        @Test
        void rejeitaNotaSemItens() {
            assertThatThrownBy(() -> NotaFiscal.criarRascunho(EMITENTE, DESTINATARIO, List.of()))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("ao menos um item");
        }

        @Test
        void rejeitaEmitenteIgualAoDestinatario() {
            assertThatThrownBy(() -> NotaFiscal.criarRascunho(EMITENTE, EMITENTE,
                    List.of(new ItemNota("Item", 1, BigDecimal.ONE))))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("diferentes");
        }
    }

    @Nested
    class MaquinaDeEstados {

        @Test
        void percorreOCaminhoFelizAteAutorizada() {
            var nota = notaEmProcessamento();
            nota.autorizar(CHAVE_VALIDA, "PROT-789", Instant.now());
            assertThat(nota.getStatus()).isEqualTo(StatusNota.AUTORIZADA);
            assertThat(nota.getChaveAcesso()).isEqualTo(CHAVE_VALIDA);
        }

        @Test
        void bloqueiaTransicaoInvalida() {
            var nota = novaNota();   // RASCUNHO
            assertThatThrownBy(() -> nota.cancelar(Instant.now()))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Transição inválida: RASCUNHO -> CANCELADA");
        }

        @Test
        void rejeitadaEEstadoFinal() {
            var nota = notaEmProcessamento();
            nota.rejeitar("Erro de schema");
            assertThatThrownBy(nota::iniciarProcessamento)
                    .isInstanceOf(DomainException.class);
        }

        @Test
        void contingenciaPermiteReprocessar() {
            var nota = notaEmProcessamento();
            nota.entrarEmContingencia();
            nota.iniciarProcessamento();
            assertThat(nota.getStatus()).isEqualTo(StatusNota.EM_PROCESSAMENTO);
        }
    }
    @Nested
    class Cancelamento {

        @Test
        void permiteDentroDaJanelaDe24h() {
            var nota = notaEmProcessamento();
            var autorizadaEm = Instant.parse("2026-07-18T10:00:00Z");
            nota.autorizar(CHAVE_VALIDA, "PROT-789", autorizadaEm);
            nota.cancelar(autorizadaEm.plus(Duration.ofHours(23)));
            assertThat(nota.getStatus()).isEqualTo(StatusNota.CANCELADA);
        }

        @Test
        void bloqueiaForaDaJanelaDe24h() {
            var nota = notaEmProcessamento();
            var autorizadaEm = Instant.parse("2026-07-18T10:00:00Z");
            nota.autorizar(CHAVE_VALIDA, "PROT-789", autorizadaEm);
            assertThatThrownBy(() -> nota.cancelar(autorizadaEm.plus(Duration.ofHours(25))))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("janela");
        }
    }
}
