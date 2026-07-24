-- Tabelas do aggregate de NotaFiscal

CREATE TABLE notas_fiscais (
                               id                     UUID PRIMARY KEY,
                               cnpj_emitente          VARCHAR(14) NOT NULL,
                               cnpj_destinatario      VARCHAR(14) NOT NULL,
                               status                 VARCHAR(30) NOT NULL,
                               chave_acesso           VARCHAR(44),
                               protocolo_autorizacao  VARCHAR(50),
                               criada_em              TIMESTAMPTZ NOT NULL,
                               autorizada_em          TIMESTAMPTZ,
                               motivo_rejeicao        VARCHAR(255)
);

CREATE TABLE itens_nota (
                            id               BIGSERIAL PRIMARY KEY,
                            nota_fiscal_id   UUID NOT NULL REFERENCES notas_fiscais(id) ON DELETE CASCADE,
                            descricao        VARCHAR(255) NOT NULL,
                            quantidade       INTEGER NOT NULL,
                            valor_unitario   NUMERIC(15,2) NOT NULL
);

CREATE INDEX idx_itens_nota_nota_fiscal_id ON itens_nota(nota_fiscal_id);
CREATE INDEX idx_notas_fiscais_status ON notas_fiscais(status);