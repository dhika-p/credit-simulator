package com.dhikaadeputra.creditsimulator.config;

import java.time.Duration;
import java.util.Map;

public final class AppConfig {
    private static final String DEFAULT_URL =
            "https://gist.githubusercontent.com/dhika-visionet/dd2d029bef57c61041d18dfbcc20e8d4/raw/1ca0655e95720effa1dfcf97134af8e397c07c44/loadtest.json";

    private String webServiceUrl;
    private Duration timeout;

    public AppConfig(String webServiceUrl, Duration timeout) {
        this.webServiceUrl = webServiceUrl;
        this.timeout = timeout;
    }

    public static AppConfig fromEnvironment(Map<String, String> env) {
        return new AppConfig(
                env.getOrDefault("CREDIT_SIMULATOR_API_URL", DEFAULT_URL),
                Duration.ofSeconds(Long.parseLong(
                        env.getOrDefault("CREDIT_SIMULATOR_TIMEOUT_SECONDS", "10"))));
    }

    public String webServiceUrl() { return webServiceUrl; }
    public Duration timeout()     { return timeout; }
}
