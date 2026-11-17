package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.normalizeCnpj;
import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.NextApiInput;
import br.com.sharpfy.nextapi.models.*;
import java.util.List;
import java.util.Map;

/** Consultas por CNPJ, com ou sem pontuação, numérico ou alfanumérico. */
public final class CnpjResource extends Resource {
    public CnpjResource(NextApiClient client) { super(client); }

    /** Dados cadastrais do estabelecimento (1 crédito). */
    public CnpjItemResponse consultar(String cnpj) { return consultar(cnpj, null); }

    /** @param toleranceDays idade máxima aceita do dado em cache, em dias (0 a 45); 0 força tempo real. */
    public CnpjItemResponse consultar(String cnpj, Integer toleranceDays) {
        return get("/v1/cnpj/" + normalizeCnpj(cnpj) + queryOf("toleranceDays", toleranceDays), CnpjItemResponse.class);
    }

    /** Consulta em tempo real ao provedor ({@code toleranceDays = 0}); exige plano que permita. */
    public CnpjItemResponse consultarAoVivo(String cnpj) { return consultar(cnpj, 0); }

    /** Situação no Simples Nacional e no MEI. */
    public CnpjSimplesResponse obterSimples(String cnpj) { return obterSimples(cnpj, null); }

    public CnpjSimplesResponse obterSimples(String cnpj, Integer toleranceDays) {
        return get("/v1/cnpj/" + normalizeCnpj(cnpj) + "/simples" + queryOf("toleranceDays", toleranceDays), CnpjSimplesResponse.class);
    }

    /** Inscrições estaduais do estabelecimento, uma por UF. */
    public List<CnpjInscricaoEstadualResponse> obterInscricoesEstaduais(String cnpj) {
        return getList("/v1/cnpj/" + normalizeCnpj(cnpj) + "/ie", CnpjInscricaoEstadualResponse.class);
    }

    /** Sanções (CEIS, CNEP, CEPIM) e dívida ativa da União. Planos pagos ou créditos avulsos. */
    public CnpjConformidadeResponse obterConformidade(String cnpj) {
        return get("/v1/cnpj/" + normalizeCnpj(cnpj) + "/conformidade", CnpjConformidadeResponse.class);
    }

    /** Sócios e empresas ligadas pelos mesmos sócios (grupo econômico). */
    public CnpjRelacionamentoResponse obterRelacionamentos(String cnpj) { return obterRelacionamentos(cnpj, null); }

    public CnpjRelacionamentoResponse obterRelacionamentos(String cnpj, Paginacao paginacao) {
        String q = paginacao == null ? "" : paginacao.query();
        return get("/v1/cnpj/" + normalizeCnpj(cnpj) + "/relacionamentos" + q, CnpjRelacionamentoResponse.class);
    }

    /** Até 100 CNPJs numa chamada; 1 crédito por CNPJ encontrado. Nunca repetida automaticamente. */
    public List<CnpjItemResponse> consultarLote(List<String> cnpjs) {
        return postList("/v1/cnpj/batch", Map.of("cnpjs", NextApiInput.normalizeBatch(cnpjs)), CnpjItemResponse.class);
    }

    /** Simples Nacional de até 100 CNPJs numa chamada. Nunca repetida automaticamente. */
    public List<CnpjSimplesResponse> obterSimplesLote(List<String> cnpjs) {
        return postList("/v1/cnpj/batch/simples", Map.of("cnpjs", NextApiInput.normalizeBatch(cnpjs)), CnpjSimplesResponse.class);
    }
}
