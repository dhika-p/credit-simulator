package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;

import java.util.Optional;

public class TenureRule implements ValidationRule {
    private static final int MIN_TENURE = 1;
    private static final int MAX_TENURE = 6;

    @Override
    public Optional<String> validate(LoanRequestModel request) {
        int tenure = request.getLoanTenure();
        if (tenure < MIN_TENURE || tenure > MAX_TENURE) {
            return Optional.of("Tenor harus antara " + MIN_TENURE + "-" + MAX_TENURE + " tahun, input Anda " + tenure);
        }
        return Optional.empty();
    }
}
