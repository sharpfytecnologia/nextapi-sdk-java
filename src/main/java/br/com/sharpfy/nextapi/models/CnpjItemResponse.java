// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CnpjItemResponse(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("razaoSocial") String razaoSocial,
        @JsonProperty("nomeFantasia") String nomeFantasia,
        @JsonProperty("tipo") String tipo,
        @JsonProperty("dataAbertura") String dataAbertura,
        @JsonProperty("situacaoCadastral") String situacaoCadastral,
        @JsonProperty("dataSituacaoCadastral") String dataSituacaoCadastral,
        @JsonProperty("motivoSituacaoCadastral") String motivoSituacaoCadastral,
        @JsonProperty("porte") String porte,
        @JsonProperty("capitalSocial") Double capitalSocial,
        @JsonProperty("naturezaJuridica") String naturezaJuridica,
        @JsonProperty("logradouro") String logradouro,
        @JsonProperty("numero") String numero,
        @JsonProperty("complemento") String complemento,
        @JsonProperty("cep") String cep,
        @JsonProperty("bairro") String bairro,
        @JsonProperty("municipio") String municipio,
        @JsonProperty("uf") String uf,
        @JsonProperty("email") String email,
        @JsonProperty("telefone") String telefone,
        @JsonProperty("situacaoEspecial") String situacaoEspecial,
        @JsonProperty("dataSituacaoEspecial") String dataSituacaoEspecial,
        @JsonProperty("atividadePrincipal") CnpjItemResponseCode atividadePrincipal,
        @JsonProperty("atividadeSecundaria") List<CnpjItemResponseCode> atividadeSecundaria,
        @JsonProperty("quadroDeSocios") List<CnpjItemResponsePartner> quadroDeSocios,
        @JsonProperty("dataAtualizacao") String dataAtualizacao
) {}
