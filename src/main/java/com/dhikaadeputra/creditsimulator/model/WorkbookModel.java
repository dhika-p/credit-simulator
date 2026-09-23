package com.dhikaadeputra.creditsimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkbookModel {
    private Map<String, SimulationSheetModel> sheets;

    public void save(SimulationSheetModel sheet){
        if(sheets.containsKey(sheet.getName())){
            throw new IllegalArgumentException("nama sheet sudah di gunakan");
        }
        this.sheets.put(sheet.getName(),sheet);
    }

    public Optional<SimulationSheetModel> get(String name){
        return Optional.ofNullable(sheets.get(name));
    }

    public List<SimulationSheetModel> all(){
        return new ArrayList<>(sheets.values());
    }

}
