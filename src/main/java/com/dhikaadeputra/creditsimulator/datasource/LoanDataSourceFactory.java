package com.dhikaadeputra.creditsimulator.datasource;

import com.dhikaadeputra.creditsimulator.config.AppConfig;
import com.dhikaadeputra.creditsimulator.datasource.json.SimpleJsonParser;
import com.dhikaadeputra.creditsimulator.model.SourceTypeModel;

public class LoanDataSourceFactory {

    private final AppConfig config;
    private final SimpleJsonParser parser;
    
    public LoanDataSourceFactory(AppConfig config, SimpleJsonParser parser) {
        this.config = config;
        this.parser = parser;
    }

    public LoanDataSource  create(SourceTypeModel source, String argument ) {
        return switch(source) {
            case WEB_SERVICE -> new WebServiceDataSource(
                    argument != null ? argument : config.webServiceUrl(), config.timeout(), parser);
            case FILE -> throw new UnsupportedOperationException("FileDataSource belum dibuat");
        };
    }
}
