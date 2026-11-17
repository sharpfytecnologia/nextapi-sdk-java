// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjRelacionamentoSocio(
        @JsonProperty("hashSocio") String hashSocio,
        @JsonProperty("nome") String nome,
        @JsonProperty("tipo") String tipo,
        @JsonProperty("documento") String documento,
        @JsonProperty("qualificacao") String qualificacao,
        @JsonProperty("faixaEtaria") String faixaEtaria,
        @JsonProperty("totalEmpresas") Integer totalEmpresas,
        @JsonProperty("vinculoVerificavel") Boolean vinculoVerificavel
) {}
