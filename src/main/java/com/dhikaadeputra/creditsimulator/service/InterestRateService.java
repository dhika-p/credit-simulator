package com.dhikaadeputra.creditsimulator.service;

import com.dhikaadeputra.creditsimulator.model.VehicleTypeModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InterestRateService {
    private static final BigDecimal CAR_BASE = new BigDecimal("0.08");
    private static final BigDecimal MOTORCYCLE_BASE = new BigDecimal("0.09");

    public List<BigDecimal> retes(VehicleTypeModel type, int tenure){
        BigDecimal base = type == VehicleTypeModel.CAR ?  CAR_BASE : MOTORCYCLE_BASE;
        List<BigDecimal> rateList = new ArrayList<>();
        BigDecimal cur = base;
        rateList.add(cur);

        for (int year = 2; year <= tenure; year++){
            BigDecimal incr = (year % 2 ) == 0 ? new BigDecimal("0.001") : new BigDecimal("0.005");
            cur = cur.add(incr);
            rateList.add(cur);
        }

        return rateList;
    }
}
