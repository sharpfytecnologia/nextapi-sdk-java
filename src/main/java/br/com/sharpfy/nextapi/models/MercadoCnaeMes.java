// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoCnaeMes(
        @JsonProperty("mes") String mes,
        @JsonProperty("aberturas") Long aberturas,
        @JsonProperty("baixas") Long baixas,
        @JsonProperty("saldo") Long saldo,
        @JsonProperty("parcial") Boolean parcial
) {}
