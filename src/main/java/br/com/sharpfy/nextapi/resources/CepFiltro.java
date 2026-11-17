package br.com.sharpfy.nextapi.resources;

/** Filtros de {@link CepsResource#buscar}. {@code q} (texto livre) não combina com {@code logradouro}. */
public final class CepFiltro {
    String uf, nomeMunicipio, logradouro, q;
    Integer idMunicipio, page, pageSize;

    public CepFiltro uf(String v) { uf = v; return this; }
    public CepFiltro idMunicipio(Integer v) { idMunicipio = v; return this; }
    public CepFiltro nomeMunicipio(String v) { nomeMunicipio = v; return this; }
    public CepFiltro logradouro(String v) { logradouro = v; return this; }
    public CepFiltro q(String v) { q = v; return this; }
    public CepFiltro page(Integer v) { page = v; return this; }
    public CepFiltro pageSize(Integer v) { pageSize = v; return this; }
}
