// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjRelacionamentoGrafoNode(
        @JsonProperty("id") String id,
        @JsonProperty("tipo") String tipo,
        @JsonProperty("label") String label,
        @JsonProperty("cnpjRaiz") String cnpjRaiz,
        @JsonProperty("hashSocio") String hashSocio,
        @JsonProperty("isAlvo") Boolean isAlvo,
        @JsonProperty("situacao") String situacao
) {}
