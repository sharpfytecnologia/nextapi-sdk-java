package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Chave ausente, inválida ou revogada. */
public final class NextApiAuthenticationException extends NextApiException {
    public NextApiAuthenticationException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiAuthenticationException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
