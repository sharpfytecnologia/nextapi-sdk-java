// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjRelacionamentoAlvo(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("cnpjRaiz") String cnpjRaiz,
        @JsonProperty("razaoSocial") String razaoSocial,
        @JsonProperty("nomeFantasia") String nomeFantasia,
        @JsonProperty("situacaoCadastral") String situacaoCadastral,
        @JsonProperty("capitalSocial") Double capitalSocial,
        @JsonProperty("uf") String uf,
        @JsonProperty("municipio") String municipio
) {}
