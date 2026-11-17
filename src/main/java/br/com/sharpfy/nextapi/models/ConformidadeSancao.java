// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConformidadeSancao(
        @JsonProperty("cadastro") String cadastro,
        @JsonProperty("cadastroDescricao") String cadastroDescricao,
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("codigo") String codigo,
        @JsonProperty("categoria") String categoria,
        @JsonProperty("inicio") String inicio,
        @JsonProperty("fim") String fim,
        @JsonProperty("vigente") Boolean vigente,
        @JsonProperty("orgao") String orgao,
        @JsonProperty("uf") String uf,
        @JsonProperty("esfera") String esfera,
        @JsonProperty("abrangencia") String abrangencia,
        @JsonProperty("processo") String processo,
        @JsonProperty("valorMulta") Double valorMulta
) {}
