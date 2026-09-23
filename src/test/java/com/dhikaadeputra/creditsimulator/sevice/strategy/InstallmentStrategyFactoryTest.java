package com.dhikaadeputra.creditsimulator.sevice.strategy;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.startegy.InstallmentStrategy;
import com.dhikaadeputra.creditsimulator.service.startegy.InstallmentStrategyFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstallmentStrategyFactoryTest {

    @Test
    void calculateWithExcelRef() {
        LoanRequestModel request = new LoanRequestModel();
        request.setLoanTenure(3);

        BigDecimal principal = new BigDecimal("75000000");
        List<BigDecimal> rates = List.of(
                new BigDecimal("0.08"), new BigDecimal("0.081"), new BigDecimal("0.086"));

        InstallmentStrategy strategy = new InstallmentStrategyFactory().create();
        List<YearlyInstallmentModel> result = strategy.calculate(request, principal, rates);

        assertEquals(3, result.size());
        assertMonthly("2250000.00", result.get(0));
        assertMonthly("2432250.00", result.get(1));
        assertMonthly("2641423.50", result.get(2));
    }

    private void assertMonthly(String expected, YearlyInstallmentModel actual) {
        BigDecimal actualRounded = actual.getMonthly().setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, new BigDecimal(expected).compareTo(actualRounded));
    }

}
