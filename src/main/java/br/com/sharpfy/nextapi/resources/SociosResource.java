package br.com.sharpfy.nextapi.resources;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.NextApiInput;
import br.com.sharpfy.nextapi.models.SocioEmpresasResponse;

public final class SociosResource extends Resource {
    public SociosResource(NextApiClient client) { super(client); }

    /**
     * Empresas do sócio. O hash vem de {@code obterRelacionamentos} ({@code socios[].hashSocio}); com
     * {@code vinculoVerificavel} falso ele é provisório: não guarde.
     */
    public SocioEmpresasResponse listarEmpresas(String hashSocio) { return listarEmpresas(hashSocio, null); }

    public SocioEmpresasResponse listarEmpresas(String hashSocio, Paginacao paginacao) {
        String q = paginacao == null ? "" : paginacao.query();
        return get("/v1/socios/" + NextApiInput.normalizeHashSocio(hashSocio) + "/empresas" + q, SocioEmpresasResponse.class);
    }
}
