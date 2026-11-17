package br.com.sharpfy.nextapi;

import br.com.sharpfy.nextapi.errors.NextApiValidationException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/** Normalização e validação das entradas, antes de qualquer requisição. */
public final class NextApiInput {
    private static final Pattern CNPJ = Pattern.compile("^[0-9A-Z]{12}[0-9]{2}$");
    private static final Pattern CEP = Pattern.compile("^[0-9]{8}$");
    private static final Pattern HASH = Pattern.compile("^[0-9a-fA-F]{20}$");
    private static final Pattern SEGMENT = Pattern.compile("^[0-9A-Za-z_-]+$");

    private NextApiInput() {}

    /** Tira pontuação e põe em maiúsculas. Aceita o CNPJ alfanumérico (12 alfanuméricos e 2 dígitos). */
    public static String normalizeCnpj(String cnpj) {
        String v = (cnpj == null ? "" : cnpj).replaceAll("[\\s./-]", "").toUpperCase(Locale.ROOT);
        if (!CNPJ.matcher(v).matches()) throw invalid("CNPJ inválido: \"" + cnpj + "\". Informe 14 caracteres, com ou sem pontuação.");
        return v;
    }

    /** CEP com 8 dígitos (aceita "01001-000"). */
    public static String normalizeCep(String cep) {
        String v = (cep == null ? "" : cep).replaceAll("[\\s.-]", "");
        if (!CEP.matcher(v).matches()) throw invalid("CEP inválido: \"" + cep + "\". Informe 8 dígitos.");
        return v;
    }

    /** Uso interno. */
    public static String normalizeHashSocio(String hash) {
        String v = hash == null ? "" : hash.trim();
        if (!HASH.matcher(v).matches()) throw invalid("hashSocio inválido: são 20 caracteres hexadecimais.");
        return v.toLowerCase(Locale.ROOT);
    }

    /** Uso interno. */
    public static String checkSegment(String value, String name) {
        String v = value == null ? "" : value.trim();
        if (!SEGMENT.matcher(v).matches()) throw invalid(name + " inválido: \"" + value + "\".");
        return v;
    }

    /** Uso interno. */
    public static String checkId(int id, String name) {
        if (id <= 0) throw invalid(name + " inválido: " + id + ".");
        return Integer.toString(id);
    }

    /** Uso interno. */
    public static void checkPositive(double v, String name) {
        if (!Double.isFinite(v) || v <= 0) throw invalid(name + " tem de ser maior que zero.");
    }

    /** Uso interno. */
    public static void checkRange(double v, double min, double max, String name) {
        if (!Double.isFinite(v) || v < min || v > max) throw invalid(name + " tem de estar entre " + fmt(min) + " e " + fmt(max) + ".");
    }

    /** Uso interno. */
    public static List<String> normalizeBatch(List<String> cnpjs) {
        if (cnpjs == null || cnpjs.isEmpty()) throw invalid("Informe ao menos um CNPJ.");
        if (cnpjs.size() > 100) throw invalid("O lote aceita até 100 CNPJs; vieram " + cnpjs.size() + ".");
        List<String> out = new ArrayList<>(cnpjs.size());
        for (String c : cnpjs) out.add(normalizeCnpj(c));
        return out;
    }

    /** Query string sem os valores ausentes, na ordem informada. Pares chave, valor alternados. */
    public static String queryOf(Object... pairs) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < pairs.length; i += 2) {
            Object v = pairs[i + 1];
            if (v == null) continue;
            String s = v instanceof Double d ? fmt(d) : v instanceof LocalDate d ? d.toString() : v.toString();
            if (s.isEmpty()) continue;
            b.append(b.length() == 0 ? '?' : '&')
                    .append(URLEncoder.encode((String) pairs[i], StandardCharsets.UTF_8)).append('=')
                    .append(URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20"));
        }
        return b.toString();
    }

    private static String fmt(double d) {
        return d == Math.rint(d) && Math.abs(d) < 1e15 ? Long.toString((long) d) : Double.toString(d);
    }

    private static NextApiValidationException invalid(String message) {
        return new NextApiValidationException(message);
    }
}
