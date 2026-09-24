package com.dhikaadeputra.creditsimulator.controller.command;

import java.math.BigDecimal;
import java.util.List;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import com.dhikaadeputra.creditsimulator.validation.LoanValidator;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class LoanBatchProcessor {
    private final ConsoleView view;
    private final CreditCalculatorService calculator;
    private final LoanValidator loanValidator;

    public LoanBatchProcessor(ConsoleView view, CreditCalculatorService calculator, LoanValidator loanValidator) {
        this.view = view;
        this.calculator = calculator;
        this.loanValidator = loanValidator;
    }

    public void process(WorkbookModel workbook, List<LoanRequestModel> requests) {
        for (int i = 0; i < requests.size(); i++) {
            LoanRequestModel request = requests.get(i);
            if (requests.size() > 1) {
                view.showMessage("");
                view.showMessage("=== Simulasi ke-" + (i + 1) + " dari " + requests.size() + " ===");
            }

            List<String> errors = loanValidator.validateAll(request);
            if (!errors.isEmpty()) {
                view.showErrors(errors);
                continue;
            }

            BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
            List<YearlyInstallmentModel> result = calculator.calculate(request);
            view.showResult(request, principal, result);
            workbook.setLastResult(request, result);
        }
    }
}
