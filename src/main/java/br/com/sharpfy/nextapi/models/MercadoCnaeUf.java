// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoCnaeUf(
        @JsonProperty("uf") String uf,
        @JsonProperty("ativas") Long ativas,
        @JsonProperty("participacaoPercentual") Double participacaoPercentual,
        @JsonProperty("posicao") Integer posicao
) {}
