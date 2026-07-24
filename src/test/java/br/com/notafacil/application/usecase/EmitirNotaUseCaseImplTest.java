package br.com.notafacil.application.usecase;

import br.com.notafacil.application.port.in.EmitirNotaCommand;
import br.com.notafacil.application.port.out.NotaFiscalRepository;
import br.com.notafacil.domain.model.StatusNota;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmitirNotaUseCaseImplTest {

    @Mock
    private NotaFiscalRepository repository;

    @Test
    void criaNotaValidadaEDelegaParaOPersistir() {
        var useCase = new EmitirNotaUseCaseImpl(repository);
        var command = new EmitirNotaCommand(
                "11222333000181", "11444777000161",
                List.of(new EmitirNotaCommand.ItemCommand("Item", 1, new BigDecimal("100.00"))));

        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = useCase.emitir(command);

        assertThat(resultado.getStatus()).isEqualTo(StatusNota.VALIDADA);

        var captor = ArgumentCaptor.forClass(br.com.notafacil.domain.model.NotaFiscal.class);
        verify(repository).salvar(captor.capture());
        assertThat(captor.getValue().valorTotal()).isEqualByComparingTo("100.00");
    }
}