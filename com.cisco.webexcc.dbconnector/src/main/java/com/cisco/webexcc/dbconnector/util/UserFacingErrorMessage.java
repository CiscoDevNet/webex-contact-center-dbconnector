package com.cisco.webexcc.dbconnector.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserFacingErrorMessage {

    private static final Pattern PREPARED_STATEMENT_PREFIX = Pattern.compile("(?is)^PreparedStatementCallback;\\s*SQL\\s*\\[.*?\\];\\s*");
    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");
    private static final Pattern ORACLE_COLUMN_PATTERN = Pattern.compile("(?i)columns\\s*\\(([^)]+)\\)");

    private UserFacingErrorMessage() {
    }

    public static String fromException(Throwable throwable) {
        String rawMessage = rootCauseMessage(throwable);
        if (rawMessage == null || rawMessage.isBlank()) {
            return "The request could not be completed. Please review your input and try again.";
        }

        String normalized = rawMessage.toUpperCase(Locale.ROOT);

        if (normalized.contains("ORA-00001")) {
            String columnName = extractOracleColumn(rawMessage);
            if (columnName != null) {
                return "Duplicate value detected for " + columnName + ". Please use a unique value and try again.";
            }
            return "Duplicate value detected. Please use a unique value and try again.";
        }

        if (normalized.contains("ORA-01722")) {
            return "A numeric field contains an invalid value. Please check your input types and try again.";
        }

        if (normalized.contains("MISSING REQUIRED PARAMETER")) {
            return simplify(rawMessage);
        }

        return simplify(rawMessage);
    }

    private static String simplify(String message) {
        String simplified = PREPARED_STATEMENT_PREFIX.matcher(message).replaceFirst("");
        simplified = URL_PATTERN.matcher(simplified).replaceAll("").trim();
        if (simplified.isEmpty()) {
            return "The request could not be completed. Please review your input and try again.";
        }
        return simplified;
    }

    private static String extractOracleColumn(String message) {
        Matcher matcher = ORACLE_COLUMN_PATTERN.matcher(message);
        if (!matcher.find()) {
            return null;
        }
        String columnText = matcher.group(1);
        if (columnText == null || columnText.isBlank()) {
            return null;
        }
        String[] pieces = columnText.split(",");
        return pieces[0].trim();
    }

    private static String rootCauseMessage(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }
}