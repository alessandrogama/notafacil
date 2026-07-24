package br.com.notafacil.infrastructure.adapter.in.rest;

import br.com.notafacil.application.port.in.ConsultarNotaUseCase;
import br.com.notafacil.application.port.in.EmitirNotaUseCase;
import br.com.notafacil.infrastructure.adapter.in.rest.dto.EmitirNotaRequest;
import br.com.notafacil.infrastructure.adapter.in.rest.dto.NotaFiscalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notas-fiscais")
public class NotaFiscalController {

    private final EmitirNotaUseCase emitirNotaUseCase;
    private final ConsultarNotaUseCase consultarNotaUseCase;
    private final NotaFiscalRestMapper mapper;

    public NotaFiscalController(EmitirNotaUseCase emitirNotaUseCase,
                                ConsultarNotaUseCase consultarNotaUseCase,
                                NotaFiscalRestMapper mapper) {
        this.emitirNotaUseCase = emitirNotaUseCase;
        this.consultarNotaUseCase = consultarNotaUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<NotaFiscalResponse> emitir(@Valid @RequestBody EmitirNotaRequest request) {
        var command = mapper.paraCommand(request);
        var nota = emitirNotaUseCase.emitir(command);
        var response = mapper.paraResponse(nota);
        return ResponseEntity.created(URI.create("/api/v1/notas-fiscais/" + nota.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalResponse> consultar(@PathVariable UUID id) {
        var nota = consultarNotaUseCase.consultarPorId(id);
        return ResponseEntity.ok(mapper.paraResponse(nota));
    }
}