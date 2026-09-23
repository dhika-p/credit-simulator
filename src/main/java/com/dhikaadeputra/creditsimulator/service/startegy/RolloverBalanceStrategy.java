package com.dhikaadeputra.creditsimulator.service.startegy;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class RolloverBalanceStrategy implements InstallmentStrategy{

    @Override
    public List<YearlyInstallmentModel> calculate(LoanRequestModel request, BigDecimal principal, List<BigDecimal> rates) {
        List<YearlyInstallmentModel> result = new ArrayList<>();
        for(int yr = 1 ; yr <= request.getLoanTenure(); yr++){
            BigDecimal rate = rates.get(yr - 1); // min karena di sesuikan rate
            int remainingTenure = request.getLoanTenure() - yr + 1;
            
            //total = pinjaman + bungan = pinjaman + (pinjaman * rate(%))
            BigDecimal total =  principal.add(principal.multiply(rate));

            //monthly = total / (tenor * 12) seperti di excel
            BigDecimal monthly = total.divide(BigDecimal.valueOf(remainingTenure).multiply(new BigDecimal("12")),10, RoundingMode.HALF_UP);
            BigDecimal yearly = monthly.multiply(new BigDecimal("12"));
            
            result.add(new YearlyInstallmentModel(yr, principal, rate, yearly, monthly));
            principal = total.subtract(yearly);
        }

        return result;
    }
}