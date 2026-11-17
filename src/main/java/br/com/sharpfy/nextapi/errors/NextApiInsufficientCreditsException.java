package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Sem créditos para a consulta (1004, 1006). */
public final class NextApiInsufficientCreditsException extends NextApiException {
    public NextApiInsufficientCreditsException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiInsufficientCreditsException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
