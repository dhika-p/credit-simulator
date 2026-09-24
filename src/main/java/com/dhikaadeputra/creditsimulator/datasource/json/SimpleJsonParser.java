package com.dhikaadeputra.creditsimulator.datasource.json;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dhikaadeputra.creditsimulator.exception.DataSourceException;

public class SimpleJsonParser {

    public Map<String, String> parse(String json) {
        if (json == null || !json.trim().startsWith("{") || !json.trim().endsWith("}")) {
            throw new DataSourceException("Response web service bukan objek JSON.");
        }

        String body = json.trim();
        body = body.substring(1, body.length() - 1);

        Map<String, String> result = new LinkedHashMap<>();
        for (String pair : body.split(",")) {
            if (pair.isBlank()) {
                continue;
            }
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                throw new DataSourceException("Format JSON tidak valid: " + pair.trim());
            }
            result.put(clean(keyValue[0]), clean(keyValue[1]));
        }
        return result;
    }

    private String clean(String text) {
        String trimmed = text.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }
}
