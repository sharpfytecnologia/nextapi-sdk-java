package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.models.CnpjSimplifiedResponse;
import br.com.sharpfy.nextapi.models.Page;

public final class EmpresasResource extends Resource {
    public EmpresasResource(NextApiClient client) { super(client); }

    public Page<CnpjSimplifiedResponse> pesquisar(EmpresaFiltro f) {
        return getPage("/v1/empresas" + queryOf("q", f.q, "uf", f.uf, "codigoMunicipio", f.codigoMunicipio, "cnpjRaiz", f.cnpjRaiz,
                "cnpj", f.cnpj, "dataAberturaInicio", f.dataAberturaInicio, "dataAberturaFim", f.dataAberturaFim,
                "codigoSituacaoCadastral", f.codigoSituacaoCadastral, "ceps", f.ceps, "cnaes", f.cnaes,
                "page", f.page, "pageSize", f.pageSize), CnpjSimplifiedResponse.class);
    }
}
