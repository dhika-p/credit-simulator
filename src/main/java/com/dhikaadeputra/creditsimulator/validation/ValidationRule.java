package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;

import java.util.Optional;

public interface ValidationRule {
    Optional<String> validate(LoanRequestModel request);
}
