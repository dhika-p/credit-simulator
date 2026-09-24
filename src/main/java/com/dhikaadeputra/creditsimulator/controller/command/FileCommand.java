package com.dhikaadeputra.creditsimulator.controller.command;

import java.nio.file.InvalidPathException;
import java.util.List;

import com.dhikaadeputra.creditsimulator.datasource.LoanDataSourceFactory;
import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.SourceTypeModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class FileCommand implements Command {
    private final ConsoleView view;
    private final LoanDataSourceFactory dataSourceFactory;
    private final LoanBatchProcessor processor;

    public FileCommand(ConsoleView view, LoanDataSourceFactory dataSourceFactory, LoanBatchProcessor processor) {
        this.view = view;
        this.dataSourceFactory = dataSourceFactory;
        this.processor = processor;
    }

    @Override
    public String name() {
        return "file";
    }

    @Override
    public String desc() {
        return "Baca simulasi dari file dan hitung otomatis (format: file <path>)";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        if (args.length == 0) {
            view.showError("Format: file <path>");
            return;
        }

        String path = CommandArgs.text(args);

        List<LoanRequestModel> requests;
        try {
            view.showMessage("Membaca file " + path + "...");
            requests = dataSourceFactory.create(SourceTypeModel.FILE, path).read();
        } catch (InvalidPathException e) {
            view.showError("Path file tidak valid: " + path);
            return;
        } catch (DataSourceException e) {
            view.showError(e.getMessage());
            return;
        }

        processor.process(workbook, requests);
    }
}
