package com.dhikaadeputra.creditsimulator.view;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyFormatter {

    public String format(BigDecimal amount) {
        BigDecimal rounded = amount.setScale(2, RoundingMode.HALF_UP);
        String[] parts = rounded.toPlainString().split("\\.");
        return "Rp. " + groupThousands(parts[0]) + "." + parts[1];
    }

    private String groupThousands(String digits) {
        boolean negative = digits.startsWith("-");
        if (negative) {
            digits = digits.substring(1);
        }

        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = digits.length() - 1; i >= 0; i--) {
            result.append(digits.charAt(i));
            count++;
            if (count % 3 == 0 && i != 0) {
                result.append(',');
            }
        }

        return (negative ? "-" : "") + result.reverse();
    }
}
