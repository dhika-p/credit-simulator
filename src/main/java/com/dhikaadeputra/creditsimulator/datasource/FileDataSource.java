package com.dhikaadeputra.creditsimulator.datasource;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;
import com.dhikaadeputra.creditsimulator.model.VehicleTypeModel;

public class FileDataSource implements LoanDataSource {
    private static final int COLUMN_COUNT = 6;
    private static final char BOM = 0xFEFF; // penanda awal file UTF-8 dari beberapa editor Windows

    private final Path path;

    public FileDataSource(Path path) {
        this.path = path;
    }

    @Override
    public List<LoanRequestModel> read() {
        if (!Files.isRegularFile(path)) {
            throw new DataSourceException("File tidak ditemukan atau bukan file biasa: " + path);
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataSourceException("Tidak dapat membaca file " + path + ": " + e.getMessage());
        }

        List<LoanRequestModel> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).strip();
            if (i == 0 && !line.isEmpty() && line.charAt(0) == BOM) {
                line = line.substring(1).strip();
            }
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            result.add(toRequest(line, i + 1));
        }

        if (result.isEmpty()) {
            throw new DataSourceException("File tidak berisi data simulasi: " + path);
        }
        return result;
    }

    private LoanRequestModel toRequest(String line, int lineNumber) {
        String[] columns = line.split(",", -1); //seperti format csv
        if (columns.length != COLUMN_COUNT) {
            throw new DataSourceException("Baris " + lineNumber + ": harus " + COLUMN_COUNT
                    + " kolom (jenis,kondisi,tahun,harga,tenor,dp), ditemukan " + columns.length);
        }

        try {
            LoanRequestModel request = new LoanRequestModel();
            request.setVehicleTypeModel(VehicleTypeModel.fromInput(columns[0]));
            request.setVehicleConditionModel(VehicleConditionModel.fromInput(columns[1]));
            request.setVehicleYear(Integer.parseInt(columns[2].trim()));
            request.setTotalLoanAmount(new BigDecimal(columns[3].trim()));
            request.setLoanTenure(Integer.parseInt(columns[4].trim()));
            request.setDownPayment(new BigDecimal(columns[5].trim()));
            return request;
        } catch (IllegalArgumentException e) {
            throw new DataSourceException("Baris " + lineNumber + ": " + e.getMessage());
        }
    }
}
