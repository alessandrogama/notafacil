package br.com.notafacil.application.port.in;

import java.math.BigDecimal;
import java.util.List;

public record EmitirNotaCommand(
        String cnpjEmitente,
        String cnpjDestinatario,
        List<ItemCommand> itens
) {
    public record ItemCommand(String descricao, int quantidade, BigDecimal valorUnitario) {}
}