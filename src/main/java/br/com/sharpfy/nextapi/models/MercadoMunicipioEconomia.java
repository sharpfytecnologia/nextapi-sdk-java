// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoMunicipioEconomia(
        @JsonProperty("pibMilReais") Double pibMilReais,
        @JsonProperty("pibPerCapitaReais") Double pibPerCapitaReais,
        @JsonProperty("anoPib") Integer anoPib,
        @JsonProperty("anoPopulacao") Integer anoPopulacao,
        @JsonProperty("atualizadoEm") String atualizadoEm
) {}
