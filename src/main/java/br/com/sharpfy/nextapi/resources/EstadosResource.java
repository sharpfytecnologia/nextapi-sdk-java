package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.models.EstadoItemResponse;
import br.com.sharpfy.nextapi.models.Page;

public final class EstadosResource extends Resource {
    public EstadosResource(NextApiClient client) { super(client); }

    public Page<EstadoItemResponse> listar() { return listar(null, null); }

    public Page<EstadoItemResponse> listar(Integer page, Integer pageSize) {
        return getPage("/v1/estados" + queryOf("page", page, "pageSize", pageSize), EstadoItemResponse.class);
    }
}
