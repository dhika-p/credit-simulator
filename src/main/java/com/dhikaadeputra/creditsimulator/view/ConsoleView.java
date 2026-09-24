package com.dhikaadeputra.creditsimulator.view;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;
import com.dhikaadeputra.creditsimulator.model.VehicleTypeModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;
    private final PrintStream out;
    private final CurrencyFormatter currencyFormatter;
    private final InstallmentTableFormatter installmentTableFormatter;

    public ConsoleView(Scanner scanner, PrintStream out, CurrencyFormatter currencyFormatter,
                        InstallmentTableFormatter installmentTableFormatter) {
        this.scanner = scanner;
        this.out = out;
        this.currencyFormatter = currencyFormatter;
        this.installmentTableFormatter = installmentTableFormatter;
    }

    public String prompt(String label) {
        out.print(label);
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            out.println();
            out.println("Input berakhir tiba-tiba. Aplikasi keluar.");
            System.exit(1);
            return null; // tidak akan tercapai, System.exit menghentikan JVM
        }
    }

    public void showMessage(String message) {
        out.println(message);
    }

    public void showError(String message) {
        
        out.println();
        out.println("Error: " + message);
    }

    public void showErrors(List<String> messages) {
        out.println();
        out.println("Error:");
        for (String message : messages) {
            out.println("  - " + message);
        }
    }

    public void showResult(LoanRequestModel request, BigDecimal principal, List<YearlyInstallmentModel> installments) {
        out.println();
        out.println("--- Hasil Simulasi ---");
        out.println("Jenis Kendaraan  : " + vehicleTypeLabel(request.getVehicleTypeModel()));
        out.println("Kondisi          : " + vehicleConditionLabel(request.getVehicleConditionModel()));
        out.println("Tahun Kendaraan  : " + request.getVehicleYear());
        out.println("Harga Kendaraan  : " + currencyFormatter.format(request.getTotalLoanAmount()));
        out.println("Uang Muka (DP)   : " + currencyFormatter.format(request.getDownPayment()));
        out.println("Pokok Pinjaman   : " + currencyFormatter.format(principal));
        out.println("Tenor            : " + request.getLoanTenure() + " tahun");
        out.println();

        for (String line : installmentTableFormatter.format(installments)) {
            out.println(line);
        }

        out.println();
        out.println(installmentTableFormatter.formatSummary(installments));
    }

    private String vehicleTypeLabel(VehicleTypeModel type) {
        return switch (type) {
            case CAR -> "Mobil";
            case MOTORCYCLE -> "Motor";
        };
    }

    private String vehicleConditionLabel(VehicleConditionModel condition) {
        return switch (condition) {
            case NEW -> "Baru";
            case SECOND -> "Bekas";
        };
    }
}
