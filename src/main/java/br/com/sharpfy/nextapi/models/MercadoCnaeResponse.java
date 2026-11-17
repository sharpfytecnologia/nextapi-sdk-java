// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoCnaeResponse(
        @JsonProperty("cnae") CnpjItemResponseCode cnae,
        @JsonProperty("disponivel") Boolean disponivel,
        @JsonProperty("geradoEm") String geradoEm,
        @JsonProperty("nacional") MercadoCnaeNacional nacional,
        @JsonProperty("ufs") List<MercadoCnaeUf> ufs,
        @JsonProperty("uf") MercadoCnaeUf uf,
        @JsonProperty("maioresMunicipios") List<MercadoCnaeMunicipio> maioresMunicipios,
        @JsonProperty("tendencia") List<MercadoCnaeMes> tendencia,
        @JsonProperty("avisos") List<String> avisos
) {}
