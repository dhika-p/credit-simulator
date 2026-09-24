package com.dhikaadeputra.creditsimulator.controller.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dhikaadeputra.creditsimulator.model.LoanRequestModel;
import com.dhikaadeputra.creditsimulator.model.VehicleConditionModel;
import com.dhikaadeputra.creditsimulator.model.VehicleTypeModel;
import com.dhikaadeputra.creditsimulator.model.WorkbookModel;
import com.dhikaadeputra.creditsimulator.model.YearlyInstallmentModel;
import com.dhikaadeputra.creditsimulator.service.CreditCalculatorService;
import com.dhikaadeputra.creditsimulator.validation.DownPaymentRule;
import com.dhikaadeputra.creditsimulator.validation.LoanAmountRule;
import com.dhikaadeputra.creditsimulator.validation.LoanValidator;
import com.dhikaadeputra.creditsimulator.validation.TenureRule;
import com.dhikaadeputra.creditsimulator.validation.VehicleYearRule;
import com.dhikaadeputra.creditsimulator.view.ConsoleView;

public class CalculateCommand implements Command {
    private ConsoleView view;
    private CreditCalculatorService calculator;
    private VehicleYearRule vehicleYearRule;
    private DownPaymentRule downPaymentRule;
    private LoanAmountRule loanAmountRule;
    private LoanValidator loanValidator;
    private TenureRule tenureRule;

    public CalculateCommand(ConsoleView view, CreditCalculatorService calculator,
                             VehicleYearRule vehicleYearRule, DownPaymentRule downPaymentRule,
                             LoanAmountRule loanAmountRule, LoanValidator loanValidator,
                             TenureRule tenureRule) {
        this.view = view;
        this.calculator = calculator;
        this.vehicleYearRule = vehicleYearRule;
        this.downPaymentRule = downPaymentRule;
        this.loanAmountRule = loanAmountRule;
        this.loanValidator = loanValidator;
        this.tenureRule = tenureRule;
    }

    @Override
    public String name() {
        return "calculate";
    }

    @Override
    public String desc() {
        return "Hitung simulasi kredit";
    }

    @Override
    public void execute(WorkbookModel workbook, String[] args) {
        LoanRequestModel request = new LoanRequestModel();
        request.setVehicleTypeModel(promptVehicleType());
        request.setVehicleConditionModel(promptVehicleCondition());
        request.setVehicleYear(promptVehicleYear(request));
        request.setTotalLoanAmount(promptLoanAmount(request));
        request.setLoanTenure(promptTenure(request));
        request.setDownPayment(promptDownPayment(request));

        BigDecimal principal = request.getTotalLoanAmount().subtract(request.getDownPayment());
        List<YearlyInstallmentModel> result = calculator.calculate(request);
        view.showResult(request, principal, result);
    }

    private VehicleTypeModel promptVehicleType() {
        while (true) {
            String raw = view.prompt("Jenis Kendaraan (Motor/Mobil): ");
           
            try {
                return VehicleTypeModel.fromInput(raw);
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        }
    }

    private VehicleConditionModel promptVehicleCondition() {
        while (true) {
            String raw = view.prompt("Kondisi Kendaraan (Baru/Bekas): ");
            try {
                return VehicleConditionModel.fromInput(raw);
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        }
    }

    private Integer promptVehicleYear(LoanRequestModel request) {
        while (true) {
            String raw = view.prompt("Tahun Kendaraan (4 digit): ");
            try {
                int year = Integer.parseInt(raw);
                request.setVehicleYear(year);
                Optional<String> error = vehicleYearRule.validate(request);
                if (error.isPresent()) {
                    view.showError(error.get());
                    continue;
                }
                return year;
            } catch (NumberFormatException e) {
                view.showError("Masukkan angka 4 digit yang valid");
            }
        }
    }

    private BigDecimal promptLoanAmount(LoanRequestModel request) {
        while (true) {
            String raw = view.prompt("Harga Kendaraan (maks 1 M): ");
            try {
                BigDecimal amount = new BigDecimal(raw);
                request.setTotalLoanAmount(amount);
                Optional<String> error = loanAmountRule.validate(request);
                if (error.isPresent()) {
                    view.showError(error.get());
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                view.showError("Masukkan angka yang valid");
            }
        }
    }

    private Integer promptTenure(LoanRequestModel request) {
        while (true) {
            String raw = view.prompt("Tenor Pinjaman (1-6 tahun): ");
            try {
                int tenure = Integer.parseInt(raw);
                request.setLoanTenure(tenure);
                Optional<String> error = tenureRule.validate(request);
                if (error.isPresent()) {
                    view.showError(error.get());
                    continue;
                }
                return tenure;
            } catch (NumberFormatException e) {
                view.showError("Masukkan angka yang valid");
            }
        }
    }

    private BigDecimal promptDownPayment(LoanRequestModel request) {
        while (true) {
            String raw = view.prompt("Jumlah DP: ");
            try {
                BigDecimal dp = new BigDecimal(raw);
                request.setDownPayment(dp);
                Optional<String> error = downPaymentRule.validate(request);
                if (error.isPresent()) {
                    view.showError(error.get());
                    continue;
                }
                return dp;
            } catch (NumberFormatException e) {
                view.showError("Masukkan angka yang valid");
            }
        }
    }
}
