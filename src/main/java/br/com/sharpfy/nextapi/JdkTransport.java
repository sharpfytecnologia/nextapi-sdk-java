package br.com.sharpfy.nextapi;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/** Transporte padrão sobre {@code java.net.http.HttpClient}. Nunca segue redirecionamento (a chave iria junto). */
final class JdkTransport implements Transport {
    private final HttpClient http = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public Response send(Request r) throws IOException, InterruptedException {
        HttpRequest.Builder b = HttpRequest.newBuilder(r.uri()).timeout(r.timeout());
        r.headers().forEach(b::header);
        b.method(r.method(), r.body() == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(r.body()));
        HttpResponse<byte[]> resp = http.send(b.build(), HttpResponse.BodyHandlers.ofByteArray());
        Map<String, String> headers = new HashMap<>();
        resp.headers().map().forEach((k, v) -> { if (!v.isEmpty()) headers.put(k, v.get(0)); });
        return new Response(resp.statusCode(), headers, resp.body());
    }
}
