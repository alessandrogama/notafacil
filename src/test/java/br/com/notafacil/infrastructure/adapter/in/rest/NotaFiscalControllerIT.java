package br.com.notafacil.infrastructure.adapter.in.rest;

import br.com.notafacil.IntegrationTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class NotaFiscalControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void emiteEDepoisConsultaUmaNota() throws Exception {
        var payload = """
                {
                  "cnpjEmitente": "11222333000181",
                  "cnpjDestinatario": "11444777000161",
                  "itens": [
                    {"descricao": "Consultoria", "quantidade": 1, "valorUnitario": 500.00}
                  ]
                }
                """;

        var resultado = mockMvc.perform(post("/api/v1/notas-fiscais")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("VALIDADA"))
                .andExpect(jsonPath("$.valorTotal").value(500.00))
                .andReturn();

        var id = objectMapper.readTree(resultado.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/api/v1/notas-fiscais/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cnpjEmitente").value("11222333000181"));
    }

    @Test
    void rejeitaRequisicaoSemItens() throws Exception {
        var payload = """
                {"cnpjEmitente": "11222333000181", "cnpjDestinatario": "11444777000161", "itens": []}
                """;

        mockMvc.perform(post("/api/v1/notas-fiscais")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retorna404ParaNotaInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/notas-fiscais/" + java.util.UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}