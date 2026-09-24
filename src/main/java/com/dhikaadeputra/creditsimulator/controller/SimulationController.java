package com.dhikaadeputra.creditsimulator.controller;

import com.dhikaadeputra.creditsimulator.controller.command.Command;
import com.dhikaadeputra.creditsimulator.controller.command.CommandRegistry;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

import java.util.Optional;

import javax.swing.text.View;

public class SimulationController {
    private final CommandRegistry registry;
    private final ConsoleView view;
    private final WorkbookModel workbook;

    public SimulationController(CommandRegistry registry, ConsoleView view, WorkbookModel workbook) {
        this.registry = registry;
        this.view = view;
        this.workbook = workbook;
    }

    public void run() {
        view.showMessage("=== Credit Simulator ===");
        view.showMessage("Ketik 'show' untuk melihat daftar perintah, 'exit' untuk keluar.");

        while (true) {
            String input = view.prompt("> ").trim();
            if (input.isEmpty()) {
                continue;
            }
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = input.split("\\s+", 2);
            String name = parts[0];
            String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

            Optional<Command> command = registry.find(name);
            if (command.isEmpty()) {
                view.showError("Perintah tidak dikenal: " + name);
                continue;
            }
            command.get().execute(workbook, args);
        }
    }
}
