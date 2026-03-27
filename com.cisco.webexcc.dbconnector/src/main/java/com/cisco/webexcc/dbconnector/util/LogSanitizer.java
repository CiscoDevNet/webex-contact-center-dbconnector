package com.cisco.webexcc.dbconnector.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class LogSanitizer {

    private static final String MASK = "***";

    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile(
            "(?i)(\\b(?:password|pwd|secret|token|apikey|api_key|authorization|bindpassword|username|email|phone|mobile|msisdn|notes|metadata_json)\\b\\s*[=:]\\s*)([^,;\\]\\}\\s]+|\"[^\"]*\"|'[^']*')"
    );

    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([?&][^=&\\s]+)=([^&\\s]+)");

    private static final Pattern JSON_STRING_VALUE_PATTERN = Pattern.compile("(\"[^\"]+\"\\s*:\\s*)\"(?:[^\"\\\\]|\\\\.)*\"");

    private static final Pattern SQL_STRING_LITERAL_PATTERN = Pattern.compile("'(?:''|[^'])*'");

    private LogSanitizer() {
    }

    public static String sanitize(String message) {
        if (message == null || message.isBlank()) {
            return message;
        }

        String sanitized = KEY_VALUE_PATTERN.matcher(message).replaceAll("$1" + MASK);
        sanitized = QUERY_PARAM_PATTERN.matcher(sanitized).replaceAll("$1=" + MASK);
        sanitized = JSON_STRING_VALUE_PATTERN.matcher(sanitized).replaceAll("$1\"" + MASK + "\"");
        sanitized = SQL_STRING_LITERAL_PATTERN.matcher(sanitized).replaceAll("'" + MASK + "'");
        return sanitized;
    }

    public static String sanitize(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        return sanitize(throwable.getMessage());
    }

    public static Map<String, Object> maskValues(Map<String, ?> input) {
        if (input == null || input.isEmpty()) {
            return Map.of();
        }

        Map<String, Object> masked = new LinkedHashMap<>();
        for (Map.Entry<String, ?> entry : input.entrySet()) {
            masked.put(entry.getKey(), maskValue(entry.getValue()));
        }
        return masked;
    }

    public static List<Object> maskValues(List<?> input) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }

        List<Object> masked = new ArrayList<>(input.size());
        for (Object value : input) {
            masked.add(maskValue(value));
        }
        return masked;
    }

    private static Object maskValue(Object value) {
        if (value == null) {
            return null;
        }
        return MASK;
    }
}