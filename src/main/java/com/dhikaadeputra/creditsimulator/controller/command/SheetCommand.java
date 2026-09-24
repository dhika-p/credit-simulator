package com.dhikaadeputra.creditsimulator.controller.command;

import java.math.BigDecimal;
import java.util.Optional;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.SimulationSheetModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class SheetCommand implements Command {
    private final ConsoleView view;

    public SheetCommand(ConsoleView view) {
        this.view = view;
    }

    @Override
    public String name() {
        return "sheet";
    }

    @Override
    public String desc() {
        return "Daftar sheet tersimpan, atau tampilkan hasilnya (format: sheet  atau  sheet \"<nama>\")";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        String name = CommandArgs.text(args);
        if (name.isEmpty()) {
            view.showSheets(workbook.all());
            return;
        }

        Optional<SimulationSheetModel> sheet = workbook.get(name);
        if (sheet.isEmpty()) {
            view.showError("Sheet '" + name + "' tidak ditemukan. Ketik 'sheet' untuk melihat daftarnya.");
            return;
        }

        LoanRequestModel request = sheet.get().getRequest();
        BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
        view.showMessage("");
        view.showMessage("=== Sheet: " + sheet.get().getName() + " ===");
        view.showResult(request, principal, sheet.get().getResult());
    }
}
