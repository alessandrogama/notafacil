package br.com.notafacil.domain.model;
import br.com.notafacil.domain.exception.DomainException;

public record Cnpj(String valor) {
    private static final int[] PESOS_DV1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_DV2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public Cnpj {
        if (valor == null) {
            throw new DomainException("CNPJ não pode ser nulo");
        }
        valor = valor.replaceAll("\\D", "");
        if (valor.length() != 14) {
            throw new DomainException("CNPJ deve ter 14 dígitos");
        }
        if (valor.chars().distinct().count() == 1) {
            throw new DomainException("CNPJ inválido: dígitos repetidos");
        }
        if (!validaDigito(valor, 12, PESOS_DV1) || !validaDigito(valor, 13, PESOS_DV2)) {
            throw new DomainException("CNPJ inválido: dígito verificador não confere");
        }

    }

    private static boolean validaDigito(String cnpj, int posicao, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        int esperado = resto < 2 ? 0 : 11 - resto;
        return Character.getNumericValue(cnpj.charAt(posicao)) == esperado;
    }

    public String formatado() {
        return valor.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
