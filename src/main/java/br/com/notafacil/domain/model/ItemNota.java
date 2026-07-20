package br.com.notafacil.domain.model;
import br.com.notafacil.domain.exception.DomainException;

import java.math.BigDecimal;

public record ItemNota(String descricao, int quantidade, BigDecimal valorUnitario) {

    public ItemNota {
        if (descricao == null || descricao.isBlank()) {
            throw new DomainException("Item deve ter descrição");
        }
        if (quantidade <= 0) {
            throw new DomainException("Quantidade deve ser positiva");
        }
        if (valorUnitario == null || valorUnitario.signum() <= 0) {
            throw new DomainException("Valor unitário deve ser positivo");
        }
    }
    public BigDecimal valorTotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
