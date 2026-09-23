package com.dhikaadeputra.creditsimulator.model;

import java.util.List;

public class SimulationSheetModel {
    private String name;
    private LoanRequestModel request;
    private List<YearlyInstallmentModel> result;

    public SimulationSheetModel(String name, LoanRequestModel request, List<YearlyInstallmentModel> result) {
        this.name = name;
        this.request = request;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoanRequestModel getRequest() {
        return request;
    }

    public void setRequest(LoanRequestModel request) {
        this.request = request;
    }

    public List<YearlyInstallmentModel> getResult() {
        return result;
    }

    public void setResult(List<YearlyInstallmentModel> result) {
        this.result = result;
    }
}
