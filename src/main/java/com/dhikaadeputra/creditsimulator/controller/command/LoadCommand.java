package com.dhikaadeputra.creditsimulator.controller.command;

import java.util.List;

import com.dhikaadeputra.creditsimulator.datasource.LoanDataSourceFactory;
import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.SourceTypeModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class LoadCommand implements Command {
    private final ConsoleView view;
    private final LoanDataSourceFactory dataSourceFactory;
    private final LoanBatchProcessor processor;

    public LoadCommand(ConsoleView view, LoanDataSourceFactory dataSourceFactory, LoanBatchProcessor processor) {
        this.view = view;
        this.dataSourceFactory = dataSourceFactory;
        this.processor = processor;
    }

    @Override
    public String name() {
        return "load";
    }

    @Override
    public String desc() {
        return "Ambil data dari enpoint dan hitung otomatis (opsi: -url \"<url>\")";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        String url = null;
        if (args.length > 0) {
            if (args.length != 2 || !args[0].equals("-url")) {
                view.showError("Format: load  atau  load -url \"<url>\"");
                return;
            }
            url = CommandArgs.stripQuotes(args[1]);
        }

        List<LoanRequestModel> requests;
        try {
            view.showMessage("Mengambil data dari enpoint...");
            requests = dataSourceFactory.create(SourceTypeModel.WEB_SERVICE, url).read();
        } catch (DataSourceException e) {
            view.showError(e.getMessage());
            return;
        }

        processor.process(workbook, requests);
    }
}
