package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** O CNPJ, CEP ou sócio pesquisado não existe (404). */
public final class NextApiNotFoundException extends NextApiException {
    public NextApiNotFoundException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiNotFoundException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
