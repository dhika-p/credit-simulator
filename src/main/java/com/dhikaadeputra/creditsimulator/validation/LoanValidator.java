package com.dhikaadeputra.creditsimulator.validation;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;

import java.util.ArrayList;
import java.util.List;

public class LoanValidator {
    private final List<ValidationRule> rules;

    public LoanValidator(List<ValidationRule> rules) {
        this.rules = rules;
    }

    public List<String> validateAll(LoanRequestModel request) {
        List<String> errors = new ArrayList<>();
        for (ValidationRule rule : rules) {
            rule.validate(request).ifPresent(errors::add);
        }
        return errors;
    }
}
