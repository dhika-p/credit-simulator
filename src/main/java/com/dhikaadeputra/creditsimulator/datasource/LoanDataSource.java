package com.dhikaadeputra.creditsimulator.datasource;

import java.util.List;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;

public interface LoanDataSource {
    public List<LoanRequestModel> read();
}
