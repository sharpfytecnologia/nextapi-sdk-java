// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CepItemResponse(
        @JsonProperty("cep") String cep,
        @JsonProperty("logradouro") String logradouro,
        @JsonProperty("localidade") String localidade,
        @JsonProperty("idMunicipio") Integer idMunicipio,
        @JsonProperty("nomeMunicipio") String nomeMunicipio,
        @JsonProperty("uf") String uf,
        @JsonProperty("latitude") Double latitude,
        @JsonProperty("longitude") Double longitude,
        @JsonProperty("dataAtualizacao") String dataAtualizacao,
        @JsonProperty("codigoIbge") CepCodigoIbgeResponse codigoIbge
) {}
