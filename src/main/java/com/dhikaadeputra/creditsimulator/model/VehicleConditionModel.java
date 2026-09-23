package com.dhikaadeputra.creditsimulator.model;

public enum VehicleConditionModel {
    NEW, SECOND;

    public static VehicleConditionModel fromInput(String input){
        return switch (input.trim().toUpperCase()){
            case "BARU" -> NEW;
            case "BEKAS" -> SECOND;
            default -> throw new IllegalArgumentException("kondisi mobil tidak dikenal: " + input);
        };
    }
}
