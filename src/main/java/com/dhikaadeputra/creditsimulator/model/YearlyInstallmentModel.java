package com.dhikaadeputra.creditsimulator.model;

import java.math.BigDecimal;

public class YearlyInstallmentModel {
    private Integer year;
    private BigDecimal principal;
    private BigDecimal rate;
    private BigDecimal yearly;
    private BigDecimal monthly;

    public YearlyInstallmentModel(Integer year, BigDecimal principal, BigDecimal rate, BigDecimal yearly, BigDecimal monthly) {
        this.year = year;
        this.principal = principal;
        this.rate = rate;
        this.yearly = yearly;
        this.monthly = monthly;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getYearly() {
        return yearly;
    }

    public void setYearly(BigDecimal yearly) {
        this.yearly = yearly;
    }

    public BigDecimal getMonthly() {
        return monthly;
    }

    public void setMonthly(BigDecimal monthly) {
        this.monthly = monthly;
    }
}
