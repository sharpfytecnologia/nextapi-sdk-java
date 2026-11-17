// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjRelacionamentoResumo(
        @JsonProperty("totalSocios") Integer totalSocios,
        @JsonProperty("totalEmpresasRelacionadas") Integer totalEmpresasRelacionadas,
        @JsonProperty("totalEmpresasAtivas") Integer totalEmpresasAtivas,
        @JsonProperty("totalEmpresasInativas") Integer totalEmpresasInativas,
        @JsonProperty("capitalSocialConsolidado") Double capitalSocialConsolidado,
        @JsonProperty("ufs") List<String> ufs,
        @JsonProperty("atividadesPrincipais") List<CnpjRelacionamentoAtividadeResumo> atividadesPrincipais,
        @JsonProperty("totalSociosVerificaveis") Integer totalSociosVerificaveis,
        @JsonProperty("vinculosCompletos") Boolean vinculosCompletos,
        @JsonProperty("avisos") List<String> avisos
) {}
