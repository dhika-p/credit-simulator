package com.dhikaadeputra.creditsimulator.datasource;

import com.dhikaadeputra.creditsimulator.datasource.json.SimpleJsonParser;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebServiceDataSourceTest {

    @Test
    void loadDariWebServiceLaluDihitung() throws IOException {
        String json = """
                {"vehicleType":"Mobil","vehicleCondition":"Baru","vehicleYear":2026,
                 "totalLoanAmount":500000000,"loanTenure":3,"downPayment":175000000}
                """;

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/load", exchange -> {
            byte[] body = json.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(body);
            }
        });
        server.start();

        try {
            String url = "http://127.0.0.1:" + server.getAddress().getPort() + "/load";
            LoanDataSource source = new WebServiceDataSource(url, Duration.ofSeconds(5), new SimpleJsonParser());

            LoanRequestModel request = source.read().get(0);
            List<YearlyInstallmentModel> result = new CreditCalculatorService().calculate(request);

            assertEquals(3, result.size());
            assertMonthly("9750000.00", result.get(0));
            assertMonthly("10539750.00", result.get(1));
            assertMonthly("11446168.50", result.get(2));
        } finally {
            server.stop(0);
        }
    }

    private void assertMonthly(String expected, YearlyInstallmentModel actual) {
        BigDecimal rounded = actual.getMonthly().setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, new BigDecimal(expected).compareTo(rounded));
    }
}
