package br.com.notafacil.infrastructure.adapter.in.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record EmitirNotaRequest(

        @NotBlank(message = "CNPJ do emitente é obrigatório")
        String cnpjEmitente,

        @NotBlank(message = "CNPJ do destinatário é obrigatório")
        String cnpjDestinatario,

        @NotEmpty(message = "Nota deve ter ao menos um item")
        @Valid
        List<ItemRequest> itens
) {
    public record ItemRequest(

            @NotBlank(message = "Descrição do item é obrigatória")
            String descricao,

            @Positive(message = "Quantidade deve ser positiva")
            int quantidade,

            @NotNull(message = "Valor unitário é obrigatório")
            @Positive(message = "Valor unitário deve ser positivo")
            BigDecimal valorUnitario
    ) {}
}