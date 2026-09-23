package com.dhikaadeputra.creditsimulator.model;

import java.math.BigDecimal;

public class LoanRequestModel {
    private VehicleTypeModel vehicleTypeModel;
    private VehicleConditionModel vehicleConditionModel;
    private Integer vehicleYear;
    private BigDecimal totalLoanAmount;
    private Integer loanTenure;
    private BigDecimal downPayment;

    public VehicleTypeModel getVehicleTypeModel() {
        return vehicleTypeModel;
    }

    public void setVehicleTypeModel(VehicleTypeModel vehicleTypeModel) {
        this.vehicleTypeModel = vehicleTypeModel;
    }

    public VehicleConditionModel getVehicleConditionModel() {
        return vehicleConditionModel;
    }

    public void setVehicleConditionModel(VehicleConditionModel vehicleConditionModel) {
        this.vehicleConditionModel = vehicleConditionModel;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public Integer getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(Integer loanTenure) {
        this.loanTenure = loanTenure;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }
}
