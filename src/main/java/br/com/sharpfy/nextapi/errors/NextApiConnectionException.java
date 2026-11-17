package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Não houve resposta: rede, DNS, TLS, tempo esgotado ou redirecionamento recusado. */
public final class NextApiConnectionException extends NextApiException {
    public NextApiConnectionException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiConnectionException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
