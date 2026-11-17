// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjSimplesResponse(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("opcaoPeloSimples") Boolean opcaoPeloSimples,
        @JsonProperty("dataOpcaoPeloSimples") String dataOpcaoPeloSimples,
        @JsonProperty("dataExclusaoDoSimples") String dataExclusaoDoSimples,
        @JsonProperty("opcaoPeloMEI") Boolean opcaoPeloMEI,
        @JsonProperty("dataOpcaoPeloMEI") String dataOpcaoPeloMEI,
        @JsonProperty("dataExclusaoDoMEI") String dataExclusaoDoMEI,
        @JsonProperty("dataAtualizacao") String dataAtualizacao
) {}
