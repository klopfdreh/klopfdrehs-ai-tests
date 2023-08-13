package de.klopfdreh.ai.tests.controller;

import de.klopfdreh.ai.tests.model.PromptModel;
import de.klopfdreh.ai.tests.model.SettingsModel;
import de.klopfdreh.ai.tests.services.ChatBotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static de.klopfdreh.ai.tests.model.PromptModel.PROMPT_MODEL;
import static de.klopfdreh.ai.tests.model.SettingsModel.SETTINGS_MODEL;

@Controller
public class PromptController {

    public static final String PROMPT_VIEW = "prompt";


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(PROMPT_MODEL, new PromptModel());
        return PROMPT_VIEW;
    }

    @GetMapping("/" + PROMPT_VIEW)
    public String prompt(Model model) {
        model.addAttribute(PROMPT_MODEL, new PromptModel());
        return PROMPT_VIEW;
    }
}
