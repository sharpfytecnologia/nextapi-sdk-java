package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Falha do lado da API (5xx), inclusive a do provedor em tempo real (1014). */
public final class NextApiServerException extends NextApiException {
    public NextApiServerException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiServerException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
