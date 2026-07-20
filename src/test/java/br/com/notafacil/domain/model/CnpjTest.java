package br.com.notafacil.domain.model;

import br.com.notafacil.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CnpjTest {

    @ParameterizedTest
    @ValueSource(strings = {"11222333000181", "11.222.333/0001-81", "11444777000161"})
    void aceitaCnpjValidoComOuSemMascara(String valor) {
        assertThat(new Cnpj(valor).valor()).hasSize(14);
    }

    @ParameterizedTest
    @ValueSource(strings = {"11222333000180", "123", "11111111111111", "abcdefghijklmn"})
    void rejeitaCnpjInvalido(String valor) {
        assertThatThrownBy(() -> new Cnpj(valor)).isInstanceOf(DomainException.class);
    }

    @Test
    void formataParaExibicao() {
        assertThat(new Cnpj("11222333000181").formatado()).isEqualTo("11.222.333/0001-81");
    }
}