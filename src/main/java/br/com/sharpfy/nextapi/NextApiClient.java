package br.com.sharpfy.nextapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.sharpfy.nextapi.errors.*;
import br.com.sharpfy.nextapi.resources.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Cliente da NextAPI. Thread-safe: crie um e reutilize.
 *
 * <pre>{@code
 * NextApiClient nextapi = NextApiClient.builder().build(); // lê NEXTAPI_KEY
 * CnpjItemResponse empresa = nextapi.cnpj().consultar("11.222.333/0001-81");
 * }</pre>
 */
public final class NextApiClient {
    public static final String VERSION = "0.2.0";
    public static final String DEFAULT_BASE_URL = "https://api.nextapi.com.br";
    private static final Set<Integer> RETRYABLE = Set.of(429, 502, 503, 504);

    /** Sandbox para chaves {@code nxa_test_}; o endereço é o mesmo, a API decide pela chave. */
    public enum Environment { PRODUCTION, SANDBOX }

    final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String apiKey;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final String userAgent;
    private final String clientInfo;
    private final Transport transport;
    private final Environment environment;

    private final CnpjResource cnpj = new CnpjResource(this);
    private final SociosResource socios = new SociosResource(this);
    private final CepsResource ceps = new CepsResource(this);
    private final EmpresasResource empresas = new EmpresasResource(this);
    private final CnaesResource cnaes = new CnaesResource(this);
    private final EstadosResource estados = new EstadosResource(this);
    private final MunicipiosResource municipios = new MunicipiosResource(this);
    private final MercadoResource mercado = new MercadoResource(this);
    private final CreditosResource creditos = new CreditosResource(this);

    private NextApiClient(Builder b) {
        String key = b.apiKey != null ? b.apiKey : System.getenv("NEXTAPI_KEY");
        key = key == null ? "" : key.trim();
        if (key.isEmpty()) throw new NextApiAuthenticationException("Chave de API não informada: use apiKey(...) ou defina NEXTAPI_KEY.");
        this.apiKey = key;
        this.environment = key.startsWith("nxa_test_") ? Environment.SANDBOX : Environment.PRODUCTION;
        this.baseUrl = checkBaseUrl(b.baseUrl);
        this.timeout = b.timeout;
        this.maxRetries = Math.max(0, b.maxRetries);
        String app = sanitizeAppInfo(b.appInfo);
        String runtime = "java/" + System.getProperty("java.version");
        String ua = "nextapi-java/" + VERSION + " " + runtime;
        this.userAgent = app.isEmpty() ? ua : ua + " " + app;
        this.clientInfo = runtime + "; " + osName(System.getProperty("os.name", "")) + (app.isEmpty() ? "" : "; " + app);
        this.transport = b.transport != null ? b.transport : new JdkTransport();
    }

    public static Builder builder() { return new Builder(); }

    public CnpjResource cnpj() { return cnpj; }
    public SociosResource socios() { return socios; }
    public CepsResource ceps() { return ceps; }
    public EmpresasResource empresas() { return empresas; }
    public CnaesResource cnaes() { return cnaes; }
    public EstadosResource estados() { return estados; }
    public MunicipiosResource municipios() { return municipios; }
    public MercadoResource mercado() { return mercado; }
    public CreditosResource creditos() { return creditos; }
    public Environment environment() { return environment; }

    @Override
    public String toString() { return "NextApiClient{baseUrl=" + baseUrl + ", environment=" + environment + "}"; }

    private static String checkBaseUrl(String raw) {
        URI u;
        try {
            u = URI.create(raw);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("baseUrl inválida: " + raw, e);
        }
        String host = u.getHost();
        if (u.getScheme() == null || host == null) throw new IllegalArgumentException("baseUrl inválida: " + raw);
        boolean local = host.equals("localhost") || host.equals("127.0.0.1") || host.equals("[::1]");
        if (!u.getScheme().equals("https") && !(u.getScheme().equals("http") && local))
            throw new IllegalArgumentException("baseUrl tem de usar https (http só para localhost): a chave vai no cabeçalho.");
        String s = raw;
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    /** Uso interno dos recursos. */
    public <T> T request(String method, String path, Object body, JavaType type) {
        byte[] payload;
        try {
            payload = body == null ? null : json.writeValueAsBytes(body);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        headers.put("Accept", "application/json");
        headers.put("User-Agent", userAgent);
        headers.put("X-NextApi-Client", "java/" + VERSION);
        headers.put("X-NextApi-Client-Info", clientInfo);
        if (payload != null) headers.put("Content-Type", "application/json");
        // POST de lote consome créditos: nunca repete sozinho.
        int retries = "GET".equals(method) ? maxRetries : 0;
        URI uri = URI.create(baseUrl + path);

        for (int attempt = 0; ; attempt++) {
            Transport.Response r;
            try {
                r = transport.send(new Transport.Request(method, uri, Map.copyOf(headers), payload, timeout));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NextApiConnectionException("Requisição interrompida.", 0, null, null, null, null, e);
            } catch (IOException e) {
                if (attempt < retries) {
                    sleep(backoff(attempt + 1));
                    continue;
                }
                String msg = e instanceof HttpTimeoutException
                        ? "Sem resposta da NextAPI em " + timeout.toMillis() + " ms."
                        : "Falha de conexão com a NextAPI: " + e.getMessage();
                throw new NextApiConnectionException(msg, 0, null, null, null, null, e);
            }

            int status = r.status();
            if (status >= 200 && status < 300) {
                if (r.body() == null || r.body().length == 0) return null;
                try {
                    return json.readValue(r.body(), type);
                } catch (IOException e) {
                    throw new NextApiServerException("Resposta inválida da NextAPI: " + e.getMessage(), status, null,
                            new String(r.body(), StandardCharsets.UTF_8), null, null, e);
                }
            }
            if (status >= 300 && status < 400)
                throw new NextApiConnectionException("A NextAPI respondeu com redirecionamento (" + status
                        + "); o SDK não o segue para não reenviar a chave.", status, null, null, null, null, null);

            Duration wait = retryAfter(r.header("retry-after").orElse(null));
            if (RETRYABLE.contains(status) && attempt < retries) {
                sleep(wait != null ? wait : backoff(attempt + 1));
                continue;
            }
            throw toException(r, wait);
        }
    }

    private NextApiException toException(Transport.Response r, Duration wait) {
        int status = r.status();
        String text = r.body() == null ? "" : new String(r.body(), StandardCharsets.UTF_8);
        Integer code = null;
        String message = null;
        try {
            JsonNode node = json.readTree(text);
            if (node != null && node.isObject()) {
                if (node.path("code").isInt()) code = node.get("code").asInt();
                if (node.path("message").isTextual() && !node.get("message").asText().isEmpty()) message = node.get("message").asText();
            }
        } catch (IOException ignored) {
            // Corpo que não é JSON fica só em getBody().
        }
        String msg = message != null ? message : "HTTP " + status;
        String rid = r.header("x-request-id").orElse(null);
        int c = code == null ? -1 : code;
        if (c == ErrorCodes.TOKEN_NOT_PROVIDED || c == ErrorCodes.TOKEN_INVALID || c == ErrorCodes.SANDBOX_DISABLED)
            return new NextApiAuthenticationException(msg, status, code, text, rid, wait, null);
        if (c == ErrorCodes.MISSING_CREDITS || c == ErrorCodes.LIVE_SEARCH_MISSING_CREDITS)
            return new NextApiInsufficientCreditsException(msg, status, code, text, rid, wait, null);
        if (c == ErrorCodes.REQUIRES_PAID_PLAN || c == ErrorCodes.LIVE_SEARCH_NOT_ALLOWED)
            return new NextApiPaidPlanRequiredException(msg, status, code, text, rid, wait, null);
        if (status == 401 || status == 403) return new NextApiAuthenticationException(msg, status, code, text, rid, wait, null);
        if (status == 404) return new NextApiNotFoundException(msg, status, code, text, rid, wait, null);
        if (status == 429) return new NextApiRateLimitException(msg, status, code, text, rid, wait, null);
        if (status >= 500) return new NextApiServerException(msg, status, code, text, rid, wait, null);
        if (status == 400 || status == 422) return new NextApiValidationException(msg, status, code, text, rid, wait, null);
        return new NextApiException(msg, status, code, text, rid, wait, null);
    }

    private static Duration retryAfter(String header) {
        if (header == null || header.isBlank()) return null;
        Duration d;
        try {
            d = Duration.ofMillis((long) (Double.parseDouble(header.trim()) * 1000));
        } catch (NumberFormatException e) {
            try {
                d = Duration.between(ZonedDateTime.now(), ZonedDateTime.parse(header.trim(), DateTimeFormatter.RFC_1123_DATE_TIME));
            } catch (RuntimeException ex) {
                return null;
            }
        }
        if (d.isNegative()) return Duration.ZERO;
        return d.compareTo(Duration.ofSeconds(60)) > 0 ? Duration.ofSeconds(60) : d;
    }

    private static Duration backoff(int attempt) {
        double base = Math.min(8000, 500 * Math.pow(2, attempt - 1));
        return Duration.ofMillis((long) (base / 2 + ThreadLocalRandom.current().nextDouble() * base / 2));
    }

    private static void sleep(Duration d) {
        if (d.isZero()) return;
        try {
            Thread.sleep(d.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NextApiConnectionException("Requisição interrompida.", 0, null, null, null, null, e);
        }
    }

    public static final class Builder {
        private String apiKey;
        private String baseUrl = DEFAULT_BASE_URL;
        private Duration timeout = Duration.ofSeconds(30);
        private int maxRetries = 2;
        private String appInfo;
        private Transport transport;

        /** {@code nxa_live_…} (produção) ou {@code nxa_test_…} (sandbox). Padrão: {@code NEXTAPI_KEY}. */
        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        /** Só https (http apenas para localhost). */
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        /** Tempo máximo de cada tentativa. Padrão 30 s. */
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        /** Novas tentativas em falha transitória (429, 502, 503, 504, rede), só em leituras. Padrão 2. */
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        /** Complemento do User-Agent (ex.: {@code minha-app/1.2}). */
        public Builder appInfo(String appInfo) { this.appInfo = appInfo; return this; }
        /** Transporte HTTP alternativo. */
        public Builder transport(Transport transport) { this.transport = transport; return this; }

        @Override
        public String toString() { return "NextApiClient.Builder{baseUrl=" + baseUrl + "}"; }

        public NextApiClient build() { return new NextApiClient(this); }
    }

    /** Só caracteres seguros num cabeçalho: CR/LF ou espaço no appInfo quebrariam a requisição. */
    static String sanitizeAppInfo(String info) {
        if (info == null) return "";
        String clean = info.replaceAll("[^A-Za-z0-9._/@+-]", "");
        return clean.length() > 64 ? clean.substring(0, 64) : clean;
    }

    private static String osName(String name) {
        String n = name.toLowerCase(java.util.Locale.ROOT);
        if (n.startsWith("windows")) return "windows";
        if (n.startsWith("linux")) return "linux";
        if (n.startsWith("mac")) return "darwin";
        return "other";
    }
}
