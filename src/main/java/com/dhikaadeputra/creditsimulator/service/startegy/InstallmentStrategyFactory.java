package com.dhikaadeputra.creditsimulator.service.startegy;

public class InstallmentStrategyFactory {
    public InstallmentStrategy create(){
        return new RolloverBalanceStrategy();
    }
}   