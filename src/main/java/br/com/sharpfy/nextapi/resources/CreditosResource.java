package br.com.sharpfy.nextapi.resources;

import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.models.WorkspaceCreditsResponse;

public final class CreditosResource extends Resource {
    public CreditosResource(NextApiClient client) { super(client); }

    /** Saldo de créditos do plano e avulsos, e os limites da chave. Não consome crédito. */
    public WorkspaceCreditsResponse consultar() { return get("/v1/credits", WorkspaceCreditsResponse.class); }
}
