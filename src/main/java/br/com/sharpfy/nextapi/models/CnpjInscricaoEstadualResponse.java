// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjInscricaoEstadualResponse(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("inscricaoEstadual") String inscricaoEstadual,
        @JsonProperty("uf") String uf,
        @JsonProperty("situacao") String situacao,
        @JsonProperty("cnae") String cnae,
        @JsonProperty("municipio") String municipio,
        @JsonProperty("dataAtualizacao") String dataAtualizacao
) {}
