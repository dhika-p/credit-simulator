package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class DownPaymentRule implements ValidationRule {
    private static final BigDecimal NEW_MIN_PERCENT = new BigDecimal("0.35");
    private static final BigDecimal SECOND_MIN_PERCENT = new BigDecimal("0.25");

    @Override
    public Optional<String> validate(LoanRequestModel request) {
        BigDecimal totalLoanAmount = request.getTotalLoanAmount();
        BigDecimal downPayment = request.getDownPayment();

        if (downPayment.compareTo(totalLoanAmount) >= 0) {
            return Optional.of("DP tidak boleh sama dengan atau melebihi harga kendaraan (Rp" + totalLoanAmount + ")");
        }

        BigDecimal minPercent = request.getVehicleConditionModel() == VehicleConditionModel.NEW
                ? NEW_MIN_PERCENT
                : SECOND_MIN_PERCENT;

        BigDecimal minDownPayment = totalLoanAmount
                .multiply(minPercent)
                .setScale(2, RoundingMode.HALF_UP);

        if (downPayment.compareTo(minDownPayment) < 0) {
            return Optional.of("DP minimal Rp" + minDownPayment + ", input Anda Rp" + downPayment);
        }
        return Optional.empty();
    }
}
