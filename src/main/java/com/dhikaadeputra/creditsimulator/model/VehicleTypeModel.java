package com.dhikaadeputra.creditsimulator.model;

public enum VehicleTypeModel {
    CAR, MOTORCYCLE;

    public static VehicleTypeModel fromInput(String input){
        return switch (input.trim().toUpperCase()) {
            case "MOBIL" -> CAR;
            case "MOTOR" -> MOTORCYCLE;
            default -> throw new IllegalArgumentException("Tipe kendaraan tidak dikenal: " + input);
        };
    }
}
