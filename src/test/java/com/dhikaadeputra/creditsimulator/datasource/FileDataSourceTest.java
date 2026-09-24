package com.dhikaadeputra.creditsimulator.datasource;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileDataSourceTest {

    @TempDir
    Path tempDir;

    @Test
    void bacaFileLaluDihitung() throws IOException {
        Path file = tempDir.resolve("file_inputs.txt");
        Files.writeString(file, """
                # jenis,kondisi,tahun,harga,tenor,dp
                Mobil,Bekas,2020,100000000,3,25000000

                motor , BARU , 2026 , 30000000 , 2 , 10500000
                """);

        List<LoanRequestModel> requests = new FileDataSource(file).read();
        CreditCalculatorService calculator = new CreditCalculatorService();

        assertEquals(2, requests.size());
        assertMonthly("2250000.00", calculator.calculate(requests.get(0)).get(0));
        assertMonthly("885625.00", calculator.calculate(requests.get(1)).get(0));
    }

    private void assertMonthly(String expected, YearlyInstallmentModel actual) {
        BigDecimal rounded = actual.getMonthly().setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, new BigDecimal(expected).compareTo(rounded));
    }
}
