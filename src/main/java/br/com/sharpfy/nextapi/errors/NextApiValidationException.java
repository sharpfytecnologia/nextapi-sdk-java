package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** Parâmetro rejeitado pela API (400) ou pelo próprio SDK antes de enviar (status 0). */
public final class NextApiValidationException extends NextApiException {
    public NextApiValidationException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiValidationException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
