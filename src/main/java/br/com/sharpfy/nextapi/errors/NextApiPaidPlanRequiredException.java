package br.com.sharpfy.nextapi.errors;

import java.time.Duration;

/** O recurso exige plano pago ou créditos avulsos (1012, 1005). */
public final class NextApiPaidPlanRequiredException extends NextApiException {
    public NextApiPaidPlanRequiredException(String message, int status, Integer code, String body, String requestId, Duration retryAfter, Throwable cause) {
        super(message, status, code, body, requestId, retryAfter, cause);
    }

    public NextApiPaidPlanRequiredException(String message) {
        this(message, 0, null, null, null, null, null);
    }
}
