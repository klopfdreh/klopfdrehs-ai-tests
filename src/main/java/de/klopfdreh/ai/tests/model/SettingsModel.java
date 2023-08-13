package de.klopfdreh.ai.tests.model;

import lombok.Data;

@Data
public class SettingsModel {

    public static final String SETTINGS_MODEL = "settingsModel";

    private String apiKey;

    private String url;

    private String role;

    private float temperature;

    private String model;

}
