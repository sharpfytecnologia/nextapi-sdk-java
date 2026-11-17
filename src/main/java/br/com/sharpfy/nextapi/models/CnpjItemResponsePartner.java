// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjItemResponsePartner(
        @JsonProperty("nome") String nome,
        @JsonProperty("qualificacao") String qualificacao,
        @JsonProperty("representanteLegal") String representanteLegal,
        @JsonProperty("qualificacaoRepresentanteLegal") String qualificacaoRepresentanteLegal,
        @JsonProperty("paisOrigem") String paisOrigem
) {}
