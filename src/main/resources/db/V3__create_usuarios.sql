-- V3: usuários e roles para autenticação

CREATE TABLE usuarios (
                          id          UUID PRIMARY KEY,
                          email       VARCHAR(255) NOT NULL UNIQUE,
                          senha_hash  VARCHAR(255) NOT NULL,
                          ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario_roles (
                               usuario_id  UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                               role        VARCHAR(30) NOT NULL,
                               PRIMARY KEY (usuario_id, role)
);