package br.com.sharpfy.nextapi.resources;

/** Paginação de relacionamentos e de empresas do sócio. Campos nulos não são enviados. */
public record Paginacao(Integer page, Integer pageSize, Boolean incluirInativas) {
    public static Paginacao pagina(int page, int pageSize) { return new Paginacao(page, pageSize, null); }

    public Paginacao comInativas(boolean incluir) { return new Paginacao(page, pageSize, incluir); }

    String query() { return br.com.sharpfy.nextapi.NextApiInput.queryOf("page", page, "pageSize", pageSize, "incluirInativas", incluirInativas); }
}
