package de.klopfdreh.ai.tests.controller;

import de.klopfdreh.ai.tests.model.PromptModel;
import de.klopfdreh.ai.tests.model.SettingsModel;
import de.klopfdreh.ai.tests.properties.AIApplicationProperties;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static de.klopfdreh.ai.tests.controller.PromptController.PROMPT_VIEW;
import static de.klopfdreh.ai.tests.model.PromptModel.PROMPT_MODEL;
import static de.klopfdreh.ai.tests.model.SettingsModel.SETTINGS_MODEL;

@Controller
public class SettingsController {

    public static final String SETTINGS_VIEW = "settings";

    private final AIApplicationProperties aiApplicationProperties;

    public SettingsController(AIApplicationProperties aiApplicationProperties) {
        this.aiApplicationProperties = aiApplicationProperties;
    }

    @GetMapping("/" + SETTINGS_VIEW)
    public String settings(HttpSession httpSession, Model model) {
        SettingsModel settingsModel = (SettingsModel) httpSession.getAttribute(SETTINGS_MODEL);
        settingsModel = settingsModel == null ? createSettingsModel() : settingsModel;
        model.addAttribute(SETTINGS_MODEL, settingsModel);
        return SETTINGS_VIEW;
    }

    private SettingsModel createSettingsModel() {
        SettingsModel settingsModel = new SettingsModel();
        settingsModel.setUrl(aiApplicationProperties.getUrl());
        settingsModel.setRole(aiApplicationProperties.getRole());
        settingsModel.setTemperature(aiApplicationProperties.getTemperature());
        settingsModel.setModel(aiApplicationProperties.getModel());
        return settingsModel;
    }

    @PostMapping("/" + SETTINGS_VIEW)
    public String settingsSubmit(@ModelAttribute SettingsModel settingsModel, Model model, HttpSession httpSession) {
        model.addAttribute(SETTINGS_MODEL, settingsModel);
        model.addAttribute(PROMPT_MODEL, new PromptModel());
        httpSession.setAttribute(SETTINGS_MODEL, settingsModel);
        return PROMPT_VIEW;
    }
}
