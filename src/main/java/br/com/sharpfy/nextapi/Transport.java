package br.com.sharpfy.nextapi;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Faz uma requisição HTTP e devolve a resposta, seja qual for o status. Falha de rede vira {@link IOException}.
 * O padrão usa {@code java.net.http.HttpClient}; troque para testes ou proxies corporativos.
 */
@FunctionalInterface
public interface Transport {
    Response send(Request request) throws IOException, InterruptedException;

    record Request(String method, URI uri, Map<String, String> headers, byte[] body, Duration timeout) {}

    record Response(int status, Map<String, String> headers, byte[] body) {
        /** Cabeçalho pelo nome, sem diferenciar maiúsculas. */
        public Optional<String> header(String name) {
            return headers.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name)).map(Map.Entry::getValue).findFirst();
        }
    }
}
