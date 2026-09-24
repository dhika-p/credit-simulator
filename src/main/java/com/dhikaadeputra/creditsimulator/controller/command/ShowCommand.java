package com.dhikaadeputra.creditsimulator.controller.command;

import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class ShowCommand implements Command {
    private final CommandRegistry registry;
    private final ConsoleView view;

    public ShowCommand(CommandRegistry registry, ConsoleView view) {
        this.registry = registry;
        this.view = view;
    }

    @Override
    public String name() {
        return "show";
    }

    @Override
    public String desc() {
        return "Tampilkan semua perintah yang tersedia";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        view.showMessage("Perintah yang tersedia:");
        for (Command command : registry.all()) {
            view.showMessage("  " + command.name() + " - " + command.desc());
        }
    }
}
