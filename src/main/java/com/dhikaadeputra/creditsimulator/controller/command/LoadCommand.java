package com.dhikaadeputra.creditsimulator.controller.command;

import java.math.BigDecimal;
import java.util.List;

import com.dhikaadeputra.creditsimulator.datasource.LoanDataSourceFactory;
import com.dhikaadeputra.creditsimulator.exception.DataSourceException;
import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.SourceTypeModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import com.dhikaadeputra.creditsimulator.validation.LoanValidator;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class LoadCommand implements Command {
    private final ConsoleView view;
    private final LoanDataSourceFactory dataSourceFactory;
    private final CreditCalculatorService calculator;
    private final LoanValidator loanValidator;

    public LoadCommand(ConsoleView view, LoanDataSourceFactory dataSourceFactory,
                       CreditCalculatorService calculator, LoanValidator loanValidator) {
        this.view = view;
        this.dataSourceFactory = dataSourceFactory;
        this.calculator = calculator;
        this.loanValidator = loanValidator;
    }

    @Override
    public String name() {
        return "load";
    }

    @Override
    public String desc() {
        return "Ambil data dari enpoint dan hitung otomatis";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        List<LoanRequestModel> requests;
        try {
            view.showMessage("Mengambil data dari enpoint...");
            requests = dataSourceFactory.create(SourceTypeModel.WEB_SERVICE, null).read();
        } catch (DataSourceException e) {
            view.showError(e.getMessage());
            return;
        }

        for (LoanRequestModel request : requests) {
            List<String> errors = loanValidator.validateAll(request);
            if (!errors.isEmpty()) {
                view.showErrors(errors);
                continue;
            }

            BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
            List<YearlyInstallmentModel> result = calculator.calculate(request);
            view.showResult(request, principal, result);
        }
    }
}
