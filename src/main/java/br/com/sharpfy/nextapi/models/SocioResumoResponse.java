// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SocioResumoResponse(
        @JsonProperty("totalEmpresas") Integer totalEmpresas,
        @JsonProperty("totalAtivas") Integer totalAtivas,
        @JsonProperty("totalInativas") Integer totalInativas,
        @JsonProperty("capitalSocialConsolidado") Double capitalSocialConsolidado,
        @JsonProperty("ufs") List<String> ufs,
        @JsonProperty("qualificacoes") List<String> qualificacoes
) {}
