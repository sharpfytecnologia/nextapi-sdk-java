package br.com.sharpfy.nextapi.resources;

import static br.com.sharpfy.nextapi.NextApiInput.queryOf;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.NextApiInput;
import br.com.sharpfy.nextapi.models.MercadoCnaeResponse;
import br.com.sharpfy.nextapi.models.MercadoIndicadorResponse;
import br.com.sharpfy.nextapi.models.MercadoMunicipioResponse;

/** Dados de mercado agregados. Planos pagos ou créditos avulsos. */
public final class MercadoResource extends Resource {
    public MercadoResource(NextApiClient client) { super(client); }

    /** Empresas ativas, aberturas e baixas de um CNAE (com ou sem pontuação), no Brasil ou numa UF. */
    public MercadoCnaeResponse obterCnae(String cnae) { return obterCnae(cnae, null, null); }

    public MercadoCnaeResponse obterCnae(String cnae, String uf, Integer meses) {
        String codigo = NextApiInput.checkSegment(cnae == null ? null : cnae.replaceAll("[\\s./-]", ""), "cnae");
        return get("/v1/mercado/cnaes/" + codigo + queryOf("uf", uf, "meses", meses), MercadoCnaeResponse.class);
    }

    /** Empresas e economia (PIB, população) de um município, pelo código da Receita. */
    public MercadoMunicipioResponse obterMunicipio(int id) {
        return get("/v1/mercado/municipios/" + NextApiInput.checkId(id, "id"), MercadoMunicipioResponse.class);
    }

    /** Série de um indicador econômico (ex.: {@code ipca}), de {@code inicio} a {@code fim} ({@code AAAAMM}). */
    public MercadoIndicadorResponse obterIndicador(String tipo) { return obterIndicador(tipo, null, null); }

    public MercadoIndicadorResponse obterIndicador(String tipo, String inicio, String fim) {
        return get("/v1/mercado/indicadores/" + NextApiInput.checkSegment(tipo, "tipo") + queryOf("inicio", inicio, "fim", fim),
                MercadoIndicadorResponse.class);
    }
}
