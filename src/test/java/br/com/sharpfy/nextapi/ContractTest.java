package br.com.sharpfy.nextapi;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.sharpfy.nextapi.errors.*;
import br.com.sharpfy.nextapi.resources.CepFiltro;
import br.com.sharpfy.nextapi.resources.EmpresaFiltro;
import br.com.sharpfy.nextapi.resources.Paginacao;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/** Contrato compartilhado pelos seis SDKs (sdks/contract/cases.json). */
class ContractTest {
    static final String KEY = "nxa_test_0123456789abcdef";
    static final ObjectMapper JSON = new ObjectMapper();

    /** Contrato em sdks/contract (monorepo) ou em contract/ dentro do SDK (espelho público). */
    static Path contract(String name) {
        Path local = Path.of("contract", name);
        return Files.exists(local) ? local : Path.of("..", "contract", name);
    }
    static final JsonNode CONTRACT;

    static {
        try {
            CONTRACT = JSON.readTree(Files.readString(contract("cases.json")));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    static final class Fake implements Transport {
        final List<Request> seen = new ArrayList<>();
        final IntFunction<Response> respond;

        Fake(IntFunction<Response> respond) { this.respond = respond; }

        @Override
        public Response send(Request r) { seen.add(r); return respond.apply(seen.size()); }
    }

    static Transport.Response json(int status, String body, Map<String, String> headers) {
        return new Transport.Response(status, headers, body.getBytes(StandardCharsets.UTF_8));
    }

    static NextApiClient client(Transport t) { return client(t, 2); }

    static NextApiClient client(Transport t, int retries) {
        return NextApiClient.builder().apiKey(KEY).transport(t).maxRetries(retries).build();
    }

    static String s(JsonNode o, String k) { return o.has(k) ? o.get(k).asText() : null; }
    static Integer i(JsonNode o, String k) { return o.has(k) ? o.get(k).asInt() : null; }
    static Boolean b(JsonNode o, String k) { return o.has(k) ? o.get(k).asBoolean() : null; }
    static double d(JsonNode o, String k) { return o.get(k).asDouble(); }

    /** Traduz a chamada canônica (camelCase, argumentos em JSON) para a API Java. */
    static Object invoke(NextApiClient api, String call, JsonNode args) {
        JsonNode a0 = args.size() > 0 ? args.get(0) : JSON.createObjectNode();
        JsonNode o = args.size() > 0 && args.get(args.size() - 1).isObject() ? args.get(args.size() - 1) : JSON.createObjectNode();
        String str = a0.isTextual() ? a0.asText() : null;
        List<String> list = new ArrayList<>();
        if (a0.isArray()) a0.forEach(x -> list.add(x.asText()));
        Paginacao pag = new Paginacao(i(o, "page"), i(o, "pageSize"), b(o, "incluirInativas"));
        return switch (call) {
            case "cnpj.consultar" -> api.cnpj().consultar(str, i(o, "toleranceDays"));
            case "cnpj.consultarAoVivo" -> api.cnpj().consultarAoVivo(str);
            case "cnpj.obterSimples" -> api.cnpj().obterSimples(str, i(o, "toleranceDays"));
            case "cnpj.obterInscricoesEstaduais" -> api.cnpj().obterInscricoesEstaduais(str);
            case "cnpj.obterConformidade" -> api.cnpj().obterConformidade(str);
            case "cnpj.obterRelacionamentos" -> api.cnpj().obterRelacionamentos(str, pag);
            case "cnpj.consultarLote" -> api.cnpj().consultarLote(list);
            case "cnpj.obterSimplesLote" -> api.cnpj().obterSimplesLote(list);
            case "socios.listarEmpresas" -> api.socios().listarEmpresas(str, pag);
            case "ceps.consultar" -> api.ceps().consultar(str);
            case "ceps.buscar" -> api.ceps().buscar(new CepFiltro().uf(s(o, "uf")).idMunicipio(i(o, "idMunicipio"))
                    .nomeMunicipio(s(o, "nomeMunicipio")).logradouro(s(o, "logradouro")).q(s(o, "q"))
                    .page(i(o, "page")).pageSize(i(o, "pageSize")));
            case "ceps.buscarProximos" -> api.ceps().buscarProximos(d(o, "latitude"), d(o, "longitude"), d(o, "raioMetro"), i(o, "page"), i(o, "pageSize"));
            case "ceps.buscarProximosDoCep" -> api.ceps().buscarProximosDoCep(str, d(o, "raioMetro"), i(o, "page"), i(o, "pageSize"));
            case "empresas.pesquisar" -> api.empresas().pesquisar(new EmpresaFiltro().q(s(o, "q")).uf(s(o, "uf"))
                    .codigoMunicipio(s(o, "codigoMunicipio")).cnpjRaiz(s(o, "cnpjRaiz")).cnpj(s(o, "cnpj"))
                    .dataAberturaInicio(o.has("dataAberturaInicio") ? LocalDate.parse(s(o, "dataAberturaInicio")) : null)
                    .dataAberturaFim(o.has("dataAberturaFim") ? LocalDate.parse(s(o, "dataAberturaFim")) : null)
                    .codigoSituacaoCadastral(s(o, "codigoSituacaoCadastral")).ceps(s(o, "ceps")).cnaes(s(o, "cnaes"))
                    .page(i(o, "page")).pageSize(i(o, "pageSize")));
            case "cnaes.listar" -> api.cnaes().listar(i(o, "page"), i(o, "pageSize"));
            case "estados.listar" -> api.estados().listar(i(o, "page"), i(o, "pageSize"));
            case "municipios.listar" -> api.municipios().listar(s(o, "uf"), i(o, "page"), i(o, "pageSize"));
            case "municipios.obterPorId" -> api.municipios().obterPorId(a0.asInt());
            case "mercado.obterCnae" -> api.mercado().obterCnae(str, s(o, "uf"), i(o, "meses"));
            case "mercado.obterMunicipio" -> api.mercado().obterMunicipio(a0.asInt());
            case "mercado.obterIndicador" -> api.mercado().obterIndicador(str, s(o, "inicio"), s(o, "fim"));
            case "creditos.consultar" -> api.creditos().consultar();
            default -> throw new AssertionError("SDK sem " + call);
        };
    }

    static Map<String, String> query(String raw) {
        Map<String, String> m = new TreeMap<>();
        if (raw == null || raw.isEmpty()) return m;
        for (String p : raw.split("&")) {
            String[] kv = p.split("=", 2);
            m.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return m;
    }

    @TestFactory
    Stream<DynamicTest> casos() {
        List<DynamicTest> tests = new ArrayList<>();
        CONTRACT.get("cases").forEach(c -> tests.add(DynamicTest.dynamicTest(s(c, "id"), () -> {
            Fake fake = new Fake(n -> json(200, c.get("response").toString(), Map.of()));
            assertNotNull(invoke(client(fake), c.has("call") ? s(c, "call") : s(c, "id"), c.get("args")));
            assertEquals(1, fake.seen.size());
            Transport.Request r = fake.seen.get(0);
            assertEquals(s(c, "method"), r.method());
            assertEquals("https://api.nextapi.com.br", r.uri().getScheme() + "://" + r.uri().getAuthority());
            assertEquals(s(c, "path"), r.uri().getPath());
            Map<String, String> expected = new TreeMap<>();
            c.get("query").fields().forEachRemaining(e -> expected.put(e.getKey(), e.getValue().asText()));
            assertEquals(expected, query(r.uri().getRawQuery()));
            assertEquals("Bearer " + KEY, r.headers().get("Authorization"));
            assertTrue(r.headers().get("User-Agent").matches("^nextapi-java/\\d+\\.\\d+\\.\\d+ java/.*"));
            assertTrue(r.headers().get("X-NextApi-Client").matches(CONTRACT.get("headers").get("client").asText()));
            assertTrue(r.headers().get("X-NextApi-Client-Info").matches(CONTRACT.get("headers").get("info").asText()),
                r.headers().get("X-NextApi-Client-Info"));
            if (c.has("body")) {
                assertEquals("application/json", r.headers().get("Content-Type"));
                assertEquals(c.get("body"), JSON.readTree(r.body()));
            } else {
                assertNull(r.body());
            }
        })));
        return tests.stream();
    }

    @Test
    void appInfoEFiltradoAntesDosCabecalhos() throws Exception {
        JsonNode spec = CONTRACT.get("headers").get("appInfo");
        Fake fake = new Fake(n -> json(200, "{}", Map.of()));
        NextApiClient.builder().apiKey(KEY).transport(fake).appInfo(s(spec, "input")).build().creditos().consultar();
        Map<String, String> h = fake.seen.get(0).headers();
        assertTrue(h.get("User-Agent").endsWith(" " + s(spec, "sanitized")));
        assertTrue(h.get("X-NextApi-Client-Info").endsWith("; " + s(spec, "sanitized")));
        assertTrue(h.get("X-NextApi-Client-Info").matches(CONTRACT.get("headers").get("info").asText()));
    }

    @TestFactory
    Stream<DynamicTest> entradaInvalidaNaoChegaAApi() {
        List<DynamicTest> tests = new ArrayList<>();
        CONTRACT.get("invalid").forEach(c -> tests.add(DynamicTest.dynamicTest(s(c, "reason"), () -> {
            Fake fake = new Fake(n -> json(200, "{}", Map.of()));
            NextApiValidationException e = assertThrows(NextApiValidationException.class, () -> invoke(client(fake), s(c, "call"), c.get("args")));
            assertEquals(0, e.getStatus());
            assertTrue(fake.seen.isEmpty());
        })));
        return tests.stream();
    }

    @Test
    void entradasAceitas() {
        CONTRACT.get("valid").forEach(c -> {
            Fake fake = new Fake(n -> json(200, "{}", Map.of()));
            invoke(client(fake), s(c, "call"), c.get("args"));
            assertEquals(s(c, "path"), fake.seen.get(0).uri().getPath());
        });
    }

    static final Map<String, Class<? extends NextApiException>> CLASSES = Map.of(
            "authentication", NextApiAuthenticationException.class,
            "insufficientCredits", NextApiInsufficientCreditsException.class,
            "paidPlanRequired", NextApiPaidPlanRequiredException.class,
            "notFound", NextApiNotFoundException.class,
            "validation", NextApiValidationException.class,
            "rateLimit", NextApiRateLimitException.class,
            "server", NextApiServerException.class);

    @TestFactory
    Stream<DynamicTest> erros() {
        List<DynamicTest> tests = new ArrayList<>();
        CONTRACT.get("errors").forEach(c -> tests.add(DynamicTest.dynamicTest(s(c, "status") + "/" + s(c.get("body"), "code"), () -> {
            Fake fake = new Fake(n -> json(c.get("status").asInt(), c.get("body").toString(), Map.of("X-Request-Id", "req-1", "Retry-After", "0")));
            NextApiException e = assertThrows(NextApiException.class, () -> client(fake).cnpj().consultar("11222333000181"));
            assertEquals(CLASSES.get(s(c, "error")), e.getClass());
            assertEquals(c.get("status").asInt(), e.getStatus());
            assertEquals(i(c.get("body"), "code"), e.getCode());
            assertEquals(s(c.get("body"), "message"), e.getMessage());
            assertEquals("req-1", e.getRequestId());
            assertEquals(c.path("retryable").asBoolean() ? 3 : 1, fake.seen.size());
        })));
        return tests.stream();
    }

    @Test
    void retryDe503DevolveO200() {
        Fake fake = new Fake(n -> n == 1 ? json(503, "{}", Map.of("Retry-After", "0")) : json(200, "{\"cnpj\":\"1\"}", Map.of()));
        assertEquals("1", client(fake).cnpj().consultar("11222333000181").cnpj());
        assertEquals(2, fake.seen.size());
    }

    @Test
    void loteNuncaERepetido() {
        Fake fake = new Fake(n -> json(503, "{\"code\":1014,\"message\":\"x\"}", Map.of("Retry-After", "0")));
        assertThrows(NextApiServerException.class, () -> client(fake).cnpj().consultarLote(List.of("11222333000181")));
        assertEquals(1, fake.seen.size());
    }

    @Test
    void falhaDeRedeERepetidaSemAChaveNaMensagem() {
        int[] calls = {0};
        Transport failing = r -> { calls[0]++; throw new IOException("connection refused"); };
        NextApiConnectionException e = assertThrows(NextApiConnectionException.class, () -> client(failing, 1).creditos().consultar());
        assertEquals(2, calls[0]);
        assertFalse(e.getMessage().contains(KEY));
    }

    @Test
    void tempoEsgotadoViraConnection() {
        Transport slow = r -> { throw new HttpTimeoutException("request timed out"); };
        NextApiClient api = NextApiClient.builder().apiKey(KEY).transport(slow).maxRetries(0).timeout(Duration.ofMillis(20)).build();
        NextApiConnectionException e = assertThrows(NextApiConnectionException.class, () -> api.creditos().consultar());
        assertTrue(e.getMessage().contains("20 ms"), e.getMessage());
    }

    @Test
    void redirecionamentoViraErro() {
        Fake fake = new Fake(n -> json(302, "", Map.of("Location", "https://evil.example/")));
        assertThrows(NextApiConnectionException.class, () -> client(fake).creditos().consultar());
        assertEquals(1, fake.seen.size());
    }

    @Test
    void transportePadraoNaoSegueRedirecionamento() throws Exception {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress("127.0.0.1", 0), 0);
        List<String> seen = Collections.synchronizedList(new ArrayList<>());
        server.createContext("/", ex -> {
            seen.add(ex.getRequestURI().getPath());
            ex.getResponseHeaders().add("Location", "/outro");
            ex.sendResponseHeaders(302, -1);
            ex.close();
        });
        server.start();
        try {
            NextApiClient api = NextApiClient.builder().apiKey(KEY).baseUrl("http://localhost:" + server.getAddress().getPort()).build();
            assertThrows(NextApiConnectionException.class, () -> api.creditos().consultar());
            assertEquals(List.of("/v1/credits"), seen);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void aChaveNaoAparece() {
        NextApiClient api = client(new Fake(n -> json(200, "{}", Map.of())));
        assertFalse(api.toString().contains(KEY));
        assertFalse(NextApiClient.builder().apiKey(KEY).toString().contains(KEY));
        assertEquals(NextApiClient.Environment.SANDBOX, api.environment());
    }

    @Test
    void baseUrlExigeHttps() {
        assertThrows(IllegalArgumentException.class, () -> NextApiClient.builder().apiKey(KEY).baseUrl("http://api.nextapi.com.br").build());
        assertDoesNotThrow(() -> NextApiClient.builder().apiKey(KEY).baseUrl("http://localhost:5112").build());
    }

    @Test
    void corpoDeErroQueNaoEJson() {
        Fake fake = new Fake(n -> json(500, "<html>Bad Gateway</html>", Map.of()));
        NextApiServerException e = assertThrows(NextApiServerException.class, () -> client(fake, 0).creditos().consultar());
        assertEquals("HTTP 500", e.getMessage());
        assertEquals("<html>Bad Gateway</html>", e.getBody());
    }

    @Test
    void cnpjAlfanumerico() {
        assertEquals("12ABC34501DE35", NextApiInput.normalizeCnpj("12.abc.345/01de-35"));
    }

    // ---- Fumaça contra a API publicada (sdks/smoke.sh): só roda com NEXTAPI_SMOKE_KEY. ----
    @Test
    void smoke() throws IOException {
        String key = System.getenv("NEXTAPI_SMOKE_KEY");
        org.junit.jupiter.api.Assumptions.assumeTrue(key != null && !key.isEmpty(), "fumaça: defina NEXTAPI_SMOKE_KEY");
        String env = Optional.ofNullable(System.getenv("NEXTAPI_SMOKE_ENV")).orElse("production");
        List<String> failures = new ArrayList<>();
        for (JsonNode step : JSON.readTree(Files.readString(contract("smoke.json"))).get("steps")) {
            if (step.has("env") && !s(step, "env").equals(env)) continue;
            String k = step.path("badKey").asBoolean() ? key.substring(0, 9) + "chave-invalida" : key;
            NextApiClient api = NextApiClient.builder().apiKey(k).maxRetries(4).build();
            String got = "ok", detail = "";
            try {
                invoke(api, s(step, "call"), step.get("args"));
            } catch (NextApiConnectionException e) {
                got = "connection";
                detail = " (" + e.getMessage() + ")";
            } catch (NextApiException e) {
                got = CLASSES.entrySet().stream().filter(x -> x.getValue() == e.getClass()).map(Map.Entry::getKey).findFirst().orElse("other");
                detail = " (" + e.getStatus() + "/" + e.getCode() + " " + e.getMessage() + " req=" + e.getRequestId() + ")";
            }
            System.out.println("SMOKE java " + env + " " + s(step, "id") + ": " + got + detail);
            boolean ok = false;
            for (JsonNode x : step.get("expect")) ok |= x.asText().equals(got);
            if (!ok) failures.add(s(step, "id") + ": " + got);
        }
        assertEquals(List.of(), failures);
    }
}
