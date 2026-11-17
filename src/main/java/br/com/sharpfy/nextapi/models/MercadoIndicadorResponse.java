// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoIndicadorResponse(
        @JsonProperty("tipo") String tipo,
        @JsonProperty("descricao") String descricao,
        @JsonProperty("unidade") String unidade,
        @JsonProperty("fonte") String fonte,
        @JsonProperty("disponivel") Boolean disponivel,
        @JsonProperty("serie") List<MercadoIndicadorPonto> serie,
        @JsonProperty("avisos") List<String> avisos
) {}
