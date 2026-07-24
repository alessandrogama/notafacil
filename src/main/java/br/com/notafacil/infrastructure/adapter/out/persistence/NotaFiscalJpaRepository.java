package br.com.notafacil.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotaFiscalJpaRepository extends JpaRepository<NotaFiscalJpaEntity, UUID> {
}