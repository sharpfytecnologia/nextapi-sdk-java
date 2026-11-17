// Gerado por sdks/contract/generate.py a partir de sdks/contract/openapi.json. Não edite.
package br.com.sharpfy.nextapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/** Página de resultados das rotas de listagem. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Page<T>(int page, int pageSize, long total, List<T> items) {}
