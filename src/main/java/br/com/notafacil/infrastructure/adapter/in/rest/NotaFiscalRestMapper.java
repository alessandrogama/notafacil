package br.com.notafacil.infrastructure.adapter.in.rest;

import br.com.notafacil.application.port.in.EmitirNotaCommand;
import br.com.notafacil.domain.model.ItemNota;
import br.com.notafacil.domain.model.NotaFiscal;
import br.com.notafacil.infrastructure.adapter.in.rest.dto.EmitirNotaRequest;
import br.com.notafacil.infrastructure.adapter.in.rest.dto.NotaFiscalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotaFiscalRestMapper {

    EmitirNotaCommand paraCommand(EmitirNotaRequest request);

    @Mapping(target = "cnpjEmitente", expression = "java(nota.getEmitente().valor())")
    @Mapping(target = "cnpjDestinatario", expression = "java(nota.getDestinatario().valor())")
    @Mapping(target = "status", expression = "java(nota.getStatus().name())")
    @Mapping(target = "valorTotal", expression = "java(nota.valorTotal())")
    NotaFiscalResponse paraResponse(NotaFiscal nota);

    NotaFiscalResponse.ItemResponse paraItemResponse(ItemNota item);
}