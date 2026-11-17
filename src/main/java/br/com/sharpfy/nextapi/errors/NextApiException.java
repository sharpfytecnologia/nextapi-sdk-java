package br.com.sharpfy.nextapi.errors;

import java.time.Duration;
import java.util.Optional;

/**
 * Falha numa chamada à NextAPI. A subclasse diz o que fazer; {@link #getCode()} distingue casos com o mesmo
 * status HTTP (405 tanto para "sem créditos" quanto para "plano pago obrigatório").
 */
public class NextApiException extends RuntimeException {
    private final int status;
    private final Integer code;
    private final String body;
    private final String requestId;
    private final Duration retryAfter;

    public NextApiException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.body = body;
        this.requestId = requestId;
        this.retryAfter = retryAfter;
    }

    /** Status HTTP (0 quando a requisição nem chegou à API). */
    public int getStatus() { return status; }

    /** Código de erro da NextAPI ({@link ErrorCodes}), quando informado. */
    public Integer getCode() { return code; }

    /** Corpo bruto da resposta de erro. */
    public String getBody() { return body; }

    /** Cabeçalho {@code x-request-id}, para o suporte. */
    public String getRequestId() { return requestId; }

    /** Espera indicada pelo cabeçalho {@code Retry-After}. */
    public Optional<Duration> getRetryAfter() { return Optional.ofNullable(retryAfter); }
}
