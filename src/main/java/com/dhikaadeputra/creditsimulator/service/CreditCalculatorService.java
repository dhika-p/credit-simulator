package com.dhikaadeputra.creditsimulator.service;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditCalculatorService {

    public List<YearlyInstallmentModel> calculate(LoanRequestModel request){
        // pokok = total pinjaman  - dp
        BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
        List<BigDecimal> rates = new InterestRateService().retes(request.getVehicleTypeModel(),request.getLoanTenure());
        return new ArrayList<>();
    }
}
