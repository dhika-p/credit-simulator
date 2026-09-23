package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;

import java.util.Optional;

public class VehicleYearRule implements ValidationRule {
    private final int currentYear;

    public VehicleYearRule(int currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    public Optional<String> validate(LoanRequestModel request) {
        if (request.getVehicleConditionModel() == VehicleConditionModel.NEW) {
            int minYear = currentYear - 1;
            if (request.getVehicleYear() < minYear) {
                return Optional.of("Kendaraan Baru minimal tahun " + minYear + ", input Anda " + request.getVehicleYear());
            }
        }
        return Optional.empty();
    }
}
