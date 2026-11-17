package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Limite de requisições do plano ou do sandbox (429). */
public final class NextApiRateLimitException extends NextApiException {
    public NextApiRateLimitException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiRateLimitException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
