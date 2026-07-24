package br.com.notafacil.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record NotaFiscalResponse(
        UUID id,
        String cnpjEmitente,
        String cnpjDestinatario,
        String status,
        BigDecimal valorTotal,
        Instant criadaEm,
        List<ItemResponse> itens
) {
    public record ItemResponse(String descricao, int quantidade, BigDecimal valorUnitario) {}
}