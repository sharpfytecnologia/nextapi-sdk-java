package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.NextApiInput;
import br.com.sharpfy.nextapi.models.MunicipioDetalhadoResponse;
import br.com.sharpfy.nextapi.models.MunicipioItemResponse;
import br.com.sharpfy.nextapi.models.Page;

public final class MunicipiosResource extends Resource {
    public MunicipiosResource(NextApiClient client) { super(client); }

    /** Municípios de uma UF, ou de todas com {@code uf} nulo. */
    public Page<MunicipioItemResponse> listar(String uf) { return listar(uf, null, null); }

    public Page<MunicipioItemResponse> listar(String uf, Integer page, Integer pageSize) {
        return getPage("/v1/municipios" + queryOf("uf", uf, "page", page, "pageSize", pageSize), MunicipioItemResponse.class);
    }

    /** Município pelo código da Receita, com a hierarquia do IBGE. */
    public MunicipioDetalhadoResponse obterPorId(int id) {
        return get("/v1/municipios/" + NextApiInput.checkId(id, "id"), MunicipioDetalhadoResponse.class);
    }
}
