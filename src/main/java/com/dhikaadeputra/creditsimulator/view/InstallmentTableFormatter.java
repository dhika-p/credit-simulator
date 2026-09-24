package com.dhikaadeputra.creditsimulator.view;

import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class InstallmentTableFormatter {
    private final CurrencyFormatter currencyFormatter;

    public InstallmentTableFormatter(CurrencyFormatter currencyFormatter) {
        this.currencyFormatter = currencyFormatter;
    }

    public List<String> format(List<YearlyInstallmentModel> installments) {
        List<String> lines = new ArrayList<>();
        for (YearlyInstallmentModel installment : installments) {
            lines.add(String.format("tahun %d : %s/bln , Suku Bunga : %s",
                    installment.getYear(),
                    currencyFormatter.format(installment.getMonthly()),
                    formatPercent(installment.getRate())));
        }
        return lines;
    }

    public String formatSummary(List<YearlyInstallmentModel> installments) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (YearlyInstallmentModel installment : installments) {
            totalPayment = totalPayment.add(installment.getYearly());
        }

        int totalMonths = installments.size() * 12;
        BigDecimal averageMonthly = totalPayment.divide(
                BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);

        return "Rata-rata cicilan : " + currencyFormatter.format(averageMonthly) + "/bln\n"
                + "Total pembayaran  : " + currencyFormatter.format(totalPayment);
    }

    public static String formatPercent(BigDecimal rate) {
        BigDecimal percent = rate.multiply(BigDecimal.valueOf(100)).stripTrailingZeros();
        return percent.toPlainString().replace('.', ',') + "%";
    }
}
