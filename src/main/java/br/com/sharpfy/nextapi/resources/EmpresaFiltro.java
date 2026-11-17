package br.com.sharpfy.nextapi.resources;

import java.time.LocalDate;

/** Filtros de {@link EmpresasResource#pesquisar}. {@code ceps} e {@code cnaes} aceitam vários valores separados por vírgula. */
public final class EmpresaFiltro {
    String q, uf, codigoMunicipio, cnpjRaiz, cnpj, codigoSituacaoCadastral, ceps, cnaes;
    LocalDate dataAberturaInicio, dataAberturaFim;
    Integer page, pageSize;

    public EmpresaFiltro q(String v) { q = v; return this; }
    public EmpresaFiltro uf(String v) { uf = v; return this; }
    public EmpresaFiltro codigoMunicipio(String v) { codigoMunicipio = v; return this; }
    public EmpresaFiltro cnpjRaiz(String v) { cnpjRaiz = v; return this; }
    public EmpresaFiltro cnpj(String v) { cnpj = v; return this; }
    public EmpresaFiltro dataAberturaInicio(LocalDate v) { dataAberturaInicio = v; return this; }
    public EmpresaFiltro dataAberturaFim(LocalDate v) { dataAberturaFim = v; return this; }
    public EmpresaFiltro codigoSituacaoCadastral(String v) { codigoSituacaoCadastral = v; return this; }
    public EmpresaFiltro ceps(String v) { ceps = v; return this; }
    public EmpresaFiltro cnaes(String v) { cnaes = v; return this; }
    public EmpresaFiltro page(Integer v) { page = v; return this; }
    public EmpresaFiltro pageSize(Integer v) { pageSize = v; return this; }
}
