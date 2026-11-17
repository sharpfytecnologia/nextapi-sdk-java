// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MunicipioDetalhadoResponse(
        @JsonProperty("id") Integer id,
        @JsonProperty("descricao") String descricao,
        @JsonProperty("uf") String uf,
        @JsonProperty("codigoIbge") String codigoIbge,
        @JsonProperty("populacaoEstimada") Long populacaoEstimada,
        @JsonProperty("regiao") String regiao,
        @JsonProperty("mesorregiao") MunicipioHierarquiaResponse mesorregiao,
        @JsonProperty("microrregiao") MunicipioHierarquiaResponse microrregiao,
        @JsonProperty("regiaoImediata") MunicipioHierarquiaResponse regiaoImediata,
        @JsonProperty("regiaoIntermediaria") MunicipioHierarquiaResponse regiaoIntermediaria,
        @JsonProperty("ibgeAtualizadoEm") String ibgeAtualizadoEm,
        @JsonProperty("totalEmpresas") Long totalEmpresas
) {}
