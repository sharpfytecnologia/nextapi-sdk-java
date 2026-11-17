// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoCnaeMunicipio(
        @JsonProperty("id") Integer id,
        @JsonProperty("nome") String nome,
        @JsonProperty("uf") String uf,
        @JsonProperty("ativas") Long ativas,
        @JsonProperty("populacaoEstimada") Long populacaoEstimada,
        @JsonProperty("ativasPor10MilHabitantes") Double ativasPor10MilHabitantes
) {}
