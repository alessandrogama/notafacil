package br.com.notafacil.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notas_fiscais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscalJpaEntity {
    @Id
    private UUID id;

    @Column(name = "cnpj_emitente", nullable = false, length = 14)
    private String cnpjEmitente;

    @Column(name = "cnpj_destinatario", nullable = false, length = 14)
    private String cnpjDestinatario;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "chave_acesso", length = 44)
    private String chaveAcesso;

    @Column(name = "protocolo_autorizacao", length = 50)
    private String protocoloAutorizacao;

    @Column(name = "criada_em", nullable = false)
    private Instant criadaEm;

    @Column(name = "autorizada_em")
    private Instant autorizadaEm;

    @Column(name = "motivo_rejeicao")
    private String motivoRejeicao;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default

    private List<ItemNotaJpaEntity> itens = new ArrayList<>();
}
