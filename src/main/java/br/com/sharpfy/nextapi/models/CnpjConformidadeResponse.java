// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjConformidadeResponse(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("cnpjRaiz") String cnpjRaiz,
        @JsonProperty("consultadoEm") String consultadoEm,
        @JsonProperty("sancoes") ConformidadeSancoes sancoes,
        @JsonProperty("dividaAtiva") ConformidadeDividaAtiva dividaAtiva,
        @JsonProperty("avisos") List<String> avisos
) {}
