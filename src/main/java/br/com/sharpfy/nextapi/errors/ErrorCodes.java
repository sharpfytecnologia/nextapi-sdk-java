package br.com.sharpfy.nextapi.errors;

/** Códigos de erro devolvidos pela NextAPI no corpo {@code { status, code, message }}. */
public final class ErrorCodes {
    public static final int NOT_FOUND = 1000;
    public static final int TOKEN_NOT_PROVIDED = 1001;
    public static final int TOKEN_INVALID = 1002;
    public static final int TOO_MANY_REQUESTS = 1003;
    public static final int MISSING_CREDITS = 1004;
    public static final int LIVE_SEARCH_NOT_ALLOWED = 1005;
    public static final int LIVE_SEARCH_MISSING_CREDITS = 1006;
    public static final int INVALID_CEP = 1007;
    public static final int REQUIRES_PAID_PLAN = 1012;
    public static final int EXTERNAL_PROVIDER_FAILURE = 1014;
    public static final int SANDBOX_DAILY_LIMIT = 1015;
    public static final int SANDBOX_DISABLED = 1016;

    private ErrorCodes() {}
}
