package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;

import java.math.BigDecimal;
import java.util.Optional;

public class LoanAmountRule implements ValidationRule {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000000");

    @Override
    public Optional<String> validate(LoanRequestModel request) {
        BigDecimal amount = request.getTotalLoanAmount();
        if (amount.compareTo(MAX_AMOUNT) > 0) {
            return Optional.of("Jumlah pinjaman maksimal Rp1.000.000.000, input Anda Rp" + amount);
        }
        return Optional.empty();
    }
}
