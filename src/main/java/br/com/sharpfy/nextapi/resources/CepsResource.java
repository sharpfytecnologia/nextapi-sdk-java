package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.normalizeCep;
import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.NextApiInput;
import br.com.sharpfy.nextapi.models.CepItemResponse;
import br.com.sharpfy.nextapi.models.Page;

public final class CepsResource extends Resource {
    public CepsResource(NextApiClient client) { super(client); }

    /** Endereço e coordenadas de um CEP. */
    public CepItemResponse consultar(String cep) { return get("/v1/ceps/" + normalizeCep(cep), CepItemResponse.class); }

    /** Busca por UF, município, logradouro ou texto livre. */
    public Page<CepItemResponse> buscar(CepFiltro f) {
        return getPage("/v1/ceps" + queryOf("uf", f.uf, "idMunicipio", f.idMunicipio, "nomeMunicipio", f.nomeMunicipio,
                "logradouro", f.logradouro, "q", f.q, "page", f.page, "pageSize", f.pageSize), CepItemResponse.class);
    }

    /** CEPs num raio (em metros) ao redor de uma coordenada. */
    public Page<CepItemResponse> buscarProximos(double latitude, double longitude, double raioMetro) {
        return buscarProximos(latitude, longitude, raioMetro, null, null);
    }

    public Page<CepItemResponse> buscarProximos(double latitude, double longitude, double raioMetro, Integer page, Integer pageSize) {
        NextApiInput.checkRange(latitude, -90, 90, "latitude");
        NextApiInput.checkRange(longitude, -180, 180, "longitude");
        NextApiInput.checkPositive(raioMetro, "raioMetro");
        return getPage("/v1/ceps/geo" + queryOf("latitude", latitude, "longitude", longitude, "raioMetro", raioMetro,
                "page", page, "pageSize", pageSize), CepItemResponse.class);
    }

    /** CEPs num raio (em metros) ao redor de outro CEP. */
    public Page<CepItemResponse> buscarProximosDoCep(String cep, double raioMetro) { return buscarProximosDoCep(cep, raioMetro, null, null); }

    public Page<CepItemResponse> buscarProximosDoCep(String cep, double raioMetro, Integer page, Integer pageSize) {
        String v = normalizeCep(cep);
        NextApiInput.checkPositive(raioMetro, "raioMetro");
        return getPage("/v1/ceps/geo/" + v + queryOf("raioMetro", raioMetro, "page", page, "pageSize", pageSize), CepItemResponse.class);
    }
}
