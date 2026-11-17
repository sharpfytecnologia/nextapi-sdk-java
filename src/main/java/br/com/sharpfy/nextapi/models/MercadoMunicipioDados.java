// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoMunicipioDados(
        @JsonProperty("id") Integer id,
        @JsonProperty("nome") String nome,
        @JsonProperty("uf") String uf,
        @JsonProperty("codigoIbge") String codigoIbge,
        @JsonProperty("regiao") String regiao,
        @JsonProperty("mesorregiao") String mesorregiao,
        @JsonProperty("microrregiao") String microrregiao,
        @JsonProperty("populacaoEstimada") Long populacaoEstimada,
        @JsonProperty("areaKm2") Double areaKm2,
        @JsonProperty("densidadeDemografica") Double densidadeDemografica
) {}
