package com.dhikaadeputra.creditsimulator.controller.command;

import java.util.Optional;

import com.dhikaadeputra.creditsimulator.model.SimulationSheetModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class SaveCommand implements Command {
    private final ConsoleView view;

    public SaveCommand(ConsoleView view) {
        this.view = view;
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public String desc() {
        return "Simpan hasil simulasi terakhir sebagai sheet (format: save \"<nama>\")";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        String name = CommandArgs.text(args);
        if (name.isEmpty()) {
            view.showError("Format: save \"<nama>\"");
            return;
        }

        Optional<SimulationSheetModel> last = workbook.getLastResult();
        if (last.isEmpty()) {
            view.showError("Belum ada hasil simulasi. Jalankan calculate, load, atau file terlebih dahulu.");
            return;
        }

        try {
            workbook.save(new SimulationSheetModel(name, last.get().getRequest(), last.get().getResult()));
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
            return;
        }
        view.showMessage("Sheet '" + name + "' tersimpan.");
    }
}
