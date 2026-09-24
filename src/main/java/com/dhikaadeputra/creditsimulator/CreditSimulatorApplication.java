package com.dhikaadeputra.creditsimulator;

import com.dhikaadeputra.creditsimulator.controller.SimulationController;
import com.dhikaadeputra.creditsimulator.controller.command.CalculateCommand;
import com.dhikaadeputra.creditsimulator.config.AppConfig;
import com.dhikaadeputra.creditsimulator.controller.command.CommandRegistry;
import com.dhikaadeputra.creditsimulator.controller.command.FileCommand;
import com.dhikaadeputra.creditsimulator.controller.command.LoadCommand;
import com.dhikaadeputra.creditsimulator.controller.command.LoanBatchProcessor;
import com.dhikaadeputra.creditsimulator.controller.command.SaveCommand;
import com.dhikaadeputra.creditsimulator.controller.command.SheetCommand;
import com.dhikaadeputra.creditsimulator.controller.command.ShowCommand;
import com.dhikaadeputra.creditsimulator.datasource.LoanDataSourceFactory;
import com.dhikaadeputra.creditsimulator.datasource.json.SimpleJsonParser;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import com.dhikaadeputra.creditsimulator.validation.DownPaymentRule;
import com.dhikaadeputra.creditsimulator.validation.LoanAmountRule;
import com.dhikaadeputra.creditsimulator.validation.LoanValidator;
import com.dhikaadeputra.creditsimulator.validation.TenureRule;
import com.dhikaadeputra.creditsimulator.validation.VehicleYearRule;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;
import com.dhikaadeputra.creditsimulator.view.CurrencyFormatter;
import com.dhikaadeputra.creditsimulator.view.InstallmentTableFormatter;

import java.time.Year;
import java.util.List;
import java.util.Scanner;

public class CreditSimulatorApplication {
    public static void main(String[] args) {
        CurrencyFormatter currencyFormatter = new CurrencyFormatter();
        InstallmentTableFormatter installmentTableFormatter = new InstallmentTableFormatter(currencyFormatter);
        ConsoleView view = new ConsoleView(new Scanner(System.in), System.out, currencyFormatter, installmentTableFormatter);

        CreditCalculatorService calculator = new CreditCalculatorService();

        VehicleYearRule vehicleYearRule = new VehicleYearRule(Year.now().getValue());
        DownPaymentRule downPaymentRule = new DownPaymentRule();
        LoanAmountRule loanAmountRule = new LoanAmountRule();
        TenureRule tenureRule = new TenureRule();
        LoanValidator loanValidator = new LoanValidator(List.of(
                vehicleYearRule, downPaymentRule, loanAmountRule, tenureRule));

        AppConfig config = AppConfig.fromEnvironment(System.getenv());
        LoanDataSourceFactory dataSourceFactory = new LoanDataSourceFactory(config, new SimpleJsonParser());

        WorkbookModel workbook = new WorkbookModel();

        CommandRegistry registry = new CommandRegistry();
        registry.register(new CalculateCommand(view, calculator, vehicleYearRule, downPaymentRule,
                loanAmountRule, loanValidator, tenureRule));
        LoanBatchProcessor batchProcessor = new LoanBatchProcessor(view, calculator, loanValidator);
        registry.register(new LoadCommand(view, dataSourceFactory, batchProcessor));
        registry.register(new FileCommand(view, dataSourceFactory, batchProcessor));
        registry.register(new SaveCommand(view));
        registry.register(new SheetCommand(view));
        registry.register(new ShowCommand(registry, view));

        SimulationController controller = new SimulationController(registry, view, workbook);
        controller.run(args);
    }
}
