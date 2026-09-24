package com.dhikaadeputra.creditsimulator.controller.command;

import com.dhikaadeputra.creditsimulator.model.WorkbookModel;

public interface Command {
    public String name();
    public String desc();
    public void execute(WorkbookModel workbook, String[] args);
}
