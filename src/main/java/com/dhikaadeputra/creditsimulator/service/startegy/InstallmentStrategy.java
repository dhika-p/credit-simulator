package com.dhikaadeputra.creditsimulator.service.startegy;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;

import java.math.BigDecimal;
import java.util.List;

public interface InstallmentStrategy {
    public List<YearlyInstallmentModel> calculate(LoanRequestModel request, BigDecimal principal,  List<BigDecimal> rates);
}
