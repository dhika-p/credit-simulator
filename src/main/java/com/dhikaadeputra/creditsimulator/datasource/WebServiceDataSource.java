package com.dhikaadeputra.creditsimulator.datasource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.dhikaadeputra.creditsimulator.datasource.json.SimpleJsonParser;
import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;
import com.dhikaadeputra.creditsimulator.model.VehicleTypeModel;

public class WebServiceDataSource implements LoanDataSource {
    private final String url;
    private final Duration timeout;
    private final SimpleJsonParser parser;

    public WebServiceDataSource(String url, Duration timeout, SimpleJsonParser parser) {
        this.url = url;
        this.timeout = timeout;
        this.parser = parser;
    }

    @Override
    public List<LoanRequestModel> read() {
        HttpResponse<String> response;
        try {

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(timeout)
                    .build();

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(timeout)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IllegalArgumentException e) {
            throw new DataSourceException("URL tidak valid: " + url);
        } catch (IOException e) {
            throw new DataSourceException("Tidak dapat mengambil data dari web service ");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DataSourceException("Permintaan ke web service dibatalkan.");
        }

        if (response.statusCode() != 200) {
            throw new DataSourceException("Tidak dapat mengambil data dari web service (HTTP "
                    + response.statusCode() + ").");
        }

        Map<String, String> fields = parser.parse(response.body());
        return List.of(mapperLoan(fields));
    }

    private LoanRequestModel mapperLoan(Map<String, String> fields) {
        try {
            LoanRequestModel request = new LoanRequestModel();
            request.setVehicleTypeModel(VehicleTypeModel.fromInput(required(fields, "vehicleType")));
            request.setVehicleConditionModel(VehicleConditionModel.fromInput(required(fields, "vehicleCondition")));
            request.setVehicleYear(Integer.parseInt(required(fields, "vehicleYear")));
            request.setTotalLoanAmount(new BigDecimal(required(fields, "totalLoanAmount")));
            request.setLoanTenure(Integer.parseInt(required(fields, "loanTenure")));
            request.setDownPayment(new BigDecimal(required(fields, "downPayment")));
            return request;
        } catch (IllegalArgumentException e) {   
            throw new DataSourceException("Data dari web service tidak valid: " + e.getMessage());
        }
    }

    private String required(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null) {
            throw new DataSourceException("Field '" + key + "' tidak ada di response web service.");
        }
        return value;
    }

}
