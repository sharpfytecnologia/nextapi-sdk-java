package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.models.CnaeItemResponse;
import br.com.sharpfy.nextapi.models.Page;

public final class CnaesResource extends Resource {
    public CnaesResource(NextApiClient client) { super(client); }

    public Page<CnaeItemResponse> listar() { return listar(null, null); }

    public Page<CnaeItemResponse> listar(Integer page, Integer pageSize) {
        return getPage("/v1/cnaes" + queryOf("page", page, "pageSize", pageSize), CnaeItemResponse.class);
    }
}
