// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConformidadeDividaAtiva(
        @JsonProperty("fonte") String fonte,
        @JsonProperty("disponivel") Boolean disponivel,
        @JsonProperty("referencia") String referencia,
        @JsonProperty("consta") Boolean consta,
        @JsonProperty("total") ConformidadeSoma total,
        @JsonProperty("emCobranca") ConformidadeSoma emCobranca,
        @JsonProperty("regularizada") ConformidadeSoma regularizada,
        @JsonProperty("ajuizada") ConformidadeSoma ajuizada,
        @JsonProperty("origem") ConformidadeOrigem origem,
        @JsonProperty("corresponsavel") ConformidadeSoma corresponsavel,
        @JsonProperty("inscricaoMaisAntiga") String inscricaoMaisAntiga,
        @JsonProperty("inscricaoMaisRecente") String inscricaoMaisRecente
) {}
