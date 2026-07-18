# ADR-001: Single-module Maven com Arquitetura Hexagonal por pacotes

## Status

Aceito — 2026-07-17

## Contexto

Precisamos decidir entre multi-module Maven (um módulo por camada) ou
single-module com separação por pacotes para implementar Ports & Adapters
(Arquitetura Hexagonal).

## Decisão

Single-module com pacotes `domain`, `application` e `infrastructure`,
com fronteiras garantidas por testes ArchUnit executados no CI.

Regras impostas:

1. `domain` não depende de `application` nem de `infrastructure`.
2. `domain` não depende de nenhum framework (Spring, JPA, Jackson).
3. `application` não depende de `infrastructure`.
4. `infrastructure` é a única camada que conhece as demais.

## Consequências

- (+) Navegação mais simples para revisores e recrutadores
- (+) Build mais rápido, sem overhead de reactor Maven
- (+) Violação de camada quebra o build (ArchUnit), não depende de disciplina
- (-) Fronteira não é imposta pelo compilador — mitigado pelo ArchUnit
- (-) Se o projeto crescer para múltiplos deployables, migraremos para multi-module
