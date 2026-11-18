# NextAPI para Java (`br.com.sharpfy:nextapi`)

SDK oficial da [NextAPI](https://nextapi.com.br) para Java 17+: CNPJ (inclusive alfanumérico), Simples Nacional, inscrições estaduais, conformidade, sócios, CEP com busca geográfica, pesquisa de empresas, localidades, mercado e créditos. Usa o `java.net.http.HttpClient` do JDK; a única dependência é o Jackson. As respostas são `record`s.

## Requisitos

- Java 17 ou mais recente (testado no 17 e no 26)
- Uma chave de API (veja abaixo)

## Instalação

Maven:

```xml
<dependency>
  <groupId>br.com.sharpfy</groupId>
  <artifactId>nextapi</artifactId>
  <version>0.2.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("br.com.sharpfy:nextapi:0.2.0")
```

## Chave de API

Crie a sua em [app.nextapi.com.br](https://app.nextapi.com.br). São dois tipos:

| Prefixo | Ambiente | Custo |
|---|---|---|
| `nxa_test_…` | Sandbox: dados de um subconjunto real, limites próprios e CNPJs de teste que provocam cada erro | Nenhum: nada é debitado |
| `nxa_live_…` | Produção | Créditos do seu plano |

O ambiente sai do prefixo da chave; o endereço da API é o mesmo. Guarde a chave na variável de ambiente
`NEXTAPI_KEY`, que o cliente lê quando você não passa a chave. **Nunca** a coloque no código, no repositório ou num
front-end: quem tem a chave consome os seus créditos.

## Início rápido

```java
import br.com.sharpfy.nextapi.NextApiClient;

NextApiClient nextapi = NextApiClient.builder().build(); // lê NEXTAPI_KEY

var empresa = nextapi.cnpj().consultar("11.222.333/0001-81");
System.out.println(empresa.razaoSocial() + " — " + empresa.situacaoCadastral());
```

## Configuração

```java
NextApiClient nextapi = NextApiClient.builder()
        .apiKey(System.getenv("NEXTAPI_KEY"))     // padrão: NEXTAPI_KEY
        .baseUrl("https://api.nextapi.com.br")    // só https (http apenas para localhost)
        .timeout(Duration.ofSeconds(15))          // por tentativa; padrão 30 s
        .maxRetries(3)                            // padrão 2; 0 desliga
        .appInfo("minha-app/1.4.0")               // identifica a sua aplicação
        .build();
```

| Método do builder | Padrão | Efeito |
|---|---|---|
| `apiKey` | `NEXTAPI_KEY` | Chave de API; o prefixo define o ambiente (`nextapi.environment()`) |
| `baseUrl` | `https://api.nextapi.com.br` | Endereço da API |
| `timeout` | 30 s | Tempo limite de cada tentativa |
| `maxRetries` | 2 | Novas tentativas em falha transitória, só em GET |
| `appInfo` | — | Nome e versão da sua aplicação, nos cabeçalhos |
| `transport` | `JdkTransport` | Transporte HTTP próprio (proxy, testes) |

### Spring Boot

<!-- readme-check: ignorar (depende do framework) -->
```java
@Configuration
class NextApiConfig {
    @Bean
    NextApiClient nextApiClient(@Value("${nextapi.key}") String key) {
        return NextApiClient.builder().apiKey(key).appInfo("erp/5.2").build();
    }
}
```

```properties
# application.properties: a chave vem do ambiente (NEXTAPI_KEY), nunca do arquivo versionado
nextapi.key=${NEXTAPI_KEY}
```

## Exemplos

### CNPJ

```java
// Cadastro (1 crédito). toleranceDays: idade máxima aceita do dado em cache (0 a 45); 0 força tempo real.
var empresa = nextapi.cnpj().consultar("11222333000181", 30);
var aoVivo = nextapi.cnpj().consultarAoVivo("11222333000181"); // tempo real; exige plano que permita

var simples = nextapi.cnpj().obterSimples("11222333000181");
var ies = nextapi.cnpj().obterInscricoesEstaduais("11222333000181");
var conformidade = nextapi.cnpj().obterConformidade("11222333000181"); // sanções e dívida ativa
var alfanumerico = nextapi.cnpj().consultar("12.ABC.345/01DE-35");
```

### Lotes (até 100 CNPJs)

```java
var lote = nextapi.cnpj().consultarLote(List.of("11222333000181", "45997418000153")); // 1 crédito por CNPJ encontrado
var simplesLote = nextapi.cnpj().obterSimplesLote(List.of("11222333000181"));

// Mais de 100: divida em blocos. Lote nunca é repetido sozinho; decida você numa falha.
for (int i = 0; i < todos.size(); i += 100) {
    resultados.addAll(nextapi.cnpj().consultarLote(todos.subList(i, Math.min(i + 100, todos.size()))));
}
```

### Sócios e grupo econômico

```java
var rel = nextapi.cnpj().obterRelacionamentos("11222333000181", Paginacao.pagina(1, 50));
for (var socio : rel.socios()) {
    // hashSocio só é estável quando vinculoVerificavel é true: não guarde os provisórios.
    var empresas = nextapi.socios().listarEmpresas(socio.hashSocio());
}
```

### CEP

```java
var cep = nextapi.ceps().consultar("01001-000");
var porRua = nextapi.ceps().buscar(new CepFiltro().uf("SP").nomeMunicipio("São Paulo").logradouro("Paulista"));
var raio = nextapi.ceps().buscarProximos(-23.5505, -46.6333, 500);
var vizinhos = nextapi.ceps().buscarProximosDoCep("01001000", 1000, 1, 100);
```

### Pesquisa de empresas, com paginação

```java
for (int page = 1; ; page++) {
    var pagina = nextapi.empresas().pesquisar(new EmpresaFiltro()
            .uf("SP")
            .cnaes("1091101")                       // vários: separados por vírgula
            .dataAberturaInicio(LocalDate.of(2024, 1, 1))
            .page(page)
            .pageSize(100));
    pagina.items().forEach(e -> System.out.println(e.cnpj() + " " + e.razaoSocial()));
    if ((long) page * pagina.pageSize() >= pagina.total()) break;
}
```

### Localidades, mercado e créditos

```java
var estados = nextapi.estados().listar();
var municipios = nextapi.municipios().listar("MG", 1, 100);
var bh = nextapi.municipios().obterPorId(3106200);
var cnaes = nextapi.cnaes().listar(1, 50);

var mercado = nextapi.mercado().obterCnae("1091101", "SP", 12);
var creditos = nextapi.creditos().consultar();
System.out.println("restam " + creditos.totalRemainingCredits() + " créditos");
```

As exceções do SDK são não verificadas (`RuntimeException`).

## Erros

Toda falha vira uma exceção do SDK; a classe diz o que fazer, e o código detalha.

| Classe | Quando | Status / código | O que fazer |
|---|---|---|---|
| `NextApiAuthenticationException` | Chave ausente, inválida ou revogada | 401 / 1002, 400 / 1001 | Confira a chave e o ambiente |
| `NextApiInsufficientCreditsException` | Créditos do período acabaram | 405 / 1004, 1006 | Aguarde a renovação ou compre créditos avulsos |
| `NextApiPaidPlanRequiredException` | Recurso exige plano pago | 405 / 1012, 1005 | Mude de plano |
| `NextApiNotFoundException` | CNPJ, CEP ou município não existe | 404 / 1000 | Trate como resultado, não como falha |
| `NextApiValidationException` | Entrada recusada pela API ou pelo SDK | 400 / 1007, ou status 0 | Corrija a entrada; não repita |
| `NextApiRateLimitException` | Limite de requisições por minuto | 429 / 1003 | Já repetido sozinho; diminua o ritmo |
| `NextApiServerException` | Falha da API ou do provedor externo | 5xx / 1014, 9999 | 503 é repetido sozinho; persistindo, fale com o suporte |
| `NextApiConnectionException` | Rede, tempo esgotado ou redirecionamento | status 0 | Verifique a conexão; GETs já foram repetidos |

Todas trazem o status HTTP, o código da API, a mensagem, o corpo da resposta e o **request id**. Ao falar com o
suporte, mande o request id: ele localiza a requisição nos nossos logs.

```java
import br.com.sharpfy.nextapi.errors.*;

try {
    var empresa = nextapi.cnpj().consultar(cnpj);
} catch (NextApiNotFoundException e) {
    return Optional.empty();
} catch (NextApiInsufficientCreditsException e) {
    log.warn("sem créditos ({})", e.getRequestId());
} catch (NextApiRateLimitException e) {
    Thread.sleep(e.getRetryAfter().orElse(Duration.ofSeconds(5)).toMillis());
} catch (NextApiException e) { // qualquer outra falha do SDK
    log.error("NextAPI {}/{} req={}", e.getStatus(), e.getCode(), e.getRequestId(), e);
}
```

Os códigos estão em `ErrorCodes`.

## Comportamento

**Novas tentativas.** Só requisições `GET` são repetidas, e só em falha transitória: `429`, `502`, `503`, `504` e
erro de rede. O cliente respeita o `Retry-After` da resposta (até 60 s); sem ele, espera de 0,5 s a 8 s, com
crescimento exponencial e variação aleatória. Os lotes (`POST`) **nunca** são repetidos sozinhos, porque consomem
créditos: numa falha, decida você se repete. O padrão é 2 novas tentativas; `maxRetries(0)` desliga.

**Tempo limite.** `timeout` por tentativa (padrão 30 s). Com novas tentativas, o tempo total pode passar disso.

**Validação local.** Entrada que a API recusaria com certeza nem sai do processo: vira
`NextApiValidationException` (status 0) antes da requisição. É o caso de CNPJ com tamanho ou dígitos verificadores errados,
lote vazio ou acima de 100, CEP sem 8 dígitos, raio menor ou igual a zero e latitude ou longitude fora da faixa.
CNPJ e CEP aceitam pontuação (`11.222.333/0001-81`, `01001-000`).

**CNPJ alfanumérico.** O formato novo da Receita (IN RFB 2.229/2024, a partir de julho de 2026) é aceito em todos os
métodos de CNPJ: `12.ABC.345/01DE-35` vira `12ABC34501DE35`, com letras em maiúsculas.

**Segurança.** A chave não aparece em logs, mensagens de erro nem na representação do cliente. O cliente não segue
redirecionamento, para a chave nunca ir para outro host, e só aceita `https` no `baseUrl` (`http` apenas para
`localhost`).

**Cabeçalhos enviados.** Além da autorização, cada requisição leva `User-Agent`, `X-NextApi-Client`
(`java/<versão>`) e `X-NextApi-Client-Info` (runtime, sistema e o seu `appInfo`). Servem para o suporte e para
medirmos o uso de cada versão; não carregam dado do seu negócio. O `appInfo` passa por um filtro: só
`A-Za-z0-9._/@+-`, até 64 caracteres.

**Reuso.** `NextApiClient` é imutável e thread-safe: crie um (por exemplo, como bean) e compartilhe.

## Sandbox

Com a chave `nxa_test_`, estes CNPJs provocam cada resposta da API, para você testar o tratamento de erro sem
gastar nada:

| CNPJ | Resposta |
|---|---|
| `99999991000125` | 404, não encontrado |
| `99999992000170` | 405 / 1004, sem créditos |
| `99999993000114` | 429 / 1003, limite por minuto |
| `99999994000169` | 503 / 1014, falha do provedor |
| `99999995000103` | sucesso depois de 8 s (consulta lenta) |
| `99999996000158` | 405 / 1012, exige plano pago |
| `99999997000100` | 401 / 1002, chave inválida |

O sandbox tem limite próprio de requisições por minuto e por dia, e os dados vêm de um subconjunto real: um CNPJ ou
CEP pode existir em produção e não no sandbox.

## Testes

Os testes não usam rede: conferem cada método contra o contrato da API, em `contract/cases.json` (rota, parâmetros, corpo, respostas e erros).

```bash
docker run --rm -v "$PWD":/src -w /src maven:3.9-eclipse-temurin-17 mvn -B test
```

Com `NEXTAPI_SMOKE_KEY` definida (e `NEXTAPI_SMOKE_ENV=sandbox` ou `production`), roda também a fumaça contra a API
publicada, com os passos de `contract/smoke.json`.

## Suporte e licença

Dúvidas e problemas: abra uma issue neste repositório ou escreva pelo [app.nextapi.com.br](https://app.nextapi.com.br),
com o request id do erro. Licença MIT.
