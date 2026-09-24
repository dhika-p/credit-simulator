package com.dhikaadeputra.creditsimulator.datasource.json;

import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleJsonParserTest {

    private final SimpleJsonParser parser = new SimpleJsonParser();

    @Test
    void parseObjekDatarDenganStringDanAngka() {
        String json = """
                {
                  "vehicleType": "Mobil",
                  "vehicleYear": 2026,
                  "totalLoanAmount": 500000000
                }
                """;

        Map<String, String> result = parser.parse(json);

        assertEquals(3, result.size());
        assertEquals("Mobil", result.get("vehicleType"));
        assertEquals("2026", result.get("vehicleYear"));
        assertEquals("500000000", result.get("totalLoanAmount"));
    }
}
