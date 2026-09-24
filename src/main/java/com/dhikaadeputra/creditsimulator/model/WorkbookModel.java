package com.dhikaadeputra.creditsimulator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkbookModel {
    // key huruf kecil supaya "Avanza" dan "avanza" dianggap nama yang sama
    private Map<String, SimulationSheetModel> sheets = new LinkedHashMap<>();
    private SimulationSheetModel lastResult;

    public void save(SimulationSheetModel sheet){
        String key = key(sheet.getName());
        if(sheets.containsKey(key)){
            throw new IllegalArgumentException("Nama sheet '" + sheet.getName() + "' sudah digunakan");
        }
        this.sheets.put(key, sheet);
    }

    public Optional<SimulationSheetModel> get(String name){
        return Optional.ofNullable(sheets.get(key(name)));
    }

    public List<SimulationSheetModel> all(){
        return new ArrayList<>(sheets.values());
    }

    public void setLastResult(LoanRequestModel request, List<YearlyInstallmentModel> result) {
        this.lastResult = new SimulationSheetModel(null, request, result);
    }

    public Optional<SimulationSheetModel> getLastResult() {
        return Optional.ofNullable(lastResult);
    }

    private String key(String name) {
        return name.trim().toLowerCase();
    }
}
