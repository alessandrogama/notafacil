package br.com.notafacil.domain.model;

import java.util.Set;

public enum StatusNota {
    RASCUNHO,
    VALIDADA,
    ENFILEIRADA,
    EM_PROCESSAMENTO,
    EM_CONTINGENCIA,
    AUTORIZADA,
    REJEITADA,
    CANCELADA;

    private Set<StatusNota> transicoesPermitidas;

    static{
        RASCUNHO.transicoesPermitidas = Set.of(VALIDADA);
        VALIDADA.transicoesPermitidas = Set.of(ENFILEIRADA);
        ENFILEIRADA.transicoesPermitidas = Set.of(EM_PROCESSAMENTO);
        EM_PROCESSAMENTO.transicoesPermitidas = Set.of(AUTORIZADA, REJEITADA, EM_CONTINGENCIA);
        EM_CONTINGENCIA.transicoesPermitidas = Set.of(EM_PROCESSAMENTO, REJEITADA);
        AUTORIZADA.transicoesPermitidas = Set.of(CANCELADA);
        REJEITADA.transicoesPermitidas = Set.of();
        CANCELADA.transicoesPermitidas = Set.of();
    }
    public boolean podeTransicionarPara(StatusNota destino) {
        return transicoesPermitidas.contains(destino);
    }
}
