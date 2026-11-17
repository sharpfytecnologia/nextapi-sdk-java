// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConformidadeSancoes(
        @JsonProperty("fonte") String fonte,
        @JsonProperty("disponivel") Boolean disponivel,
        @JsonProperty("referencia") String referencia,
        @JsonProperty("consta") Boolean consta,
        @JsonProperty("vigente") Boolean vigente,
        @JsonProperty("cadastrosVigentes") List<String> cadastrosVigentes,
        @JsonProperty("registros") List<ConformidadeSancao> registros
) {}
