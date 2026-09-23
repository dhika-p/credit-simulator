package com.dhikaadeputra.creditsimulator.service;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.startegy.InstallmentStrategy;
import com.dhikaadeputra.creditsimulator.service.startegy.InstallmentStrategyFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditCalculatorService {

    public List<YearlyInstallmentModel> calculate(LoanRequestModel request){
        BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
        List<BigDecimal> rates = new InterestRateService().retes(request.getVehicleTypeModel(), request.getLoanTenure());
        InstallmentStrategy strategy = new InstallmentStrategyFactory().create();
        return strategy.calculate(request, principal, rates);
    }
}
