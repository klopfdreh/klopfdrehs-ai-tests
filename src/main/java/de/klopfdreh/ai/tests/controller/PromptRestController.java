package de.klopfdreh.ai.tests.controller;

import de.klopfdreh.ai.tests.model.PromptModel;
import de.klopfdreh.ai.tests.model.SettingsModel;
import de.klopfdreh.ai.tests.services.ChatBotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static de.klopfdreh.ai.tests.model.PromptModel.PROMPT_MODEL;
import static de.klopfdreh.ai.tests.model.SettingsModel.SETTINGS_MODEL;

@RestController
public class PromptRestController {

    public static final String PROMPT_REST_VIEW = "promptRest";

    private final ChatBotService chatBotService;

    public PromptRestController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/" + PROMPT_REST_VIEW)
    public PromptModel.Chat submitPrompt(@ModelAttribute PromptModel submittedPromptModel,
                                         HttpSession httpSession,
                                         @Nullable @RequestParam("clearKey") String clearKey) {
        if ("true".equals(clearKey)) {
            httpSession.setAttribute(PROMPT_MODEL, createPromptModel());
            return new PromptModel.Chat();
        }

        PromptModel promptModel = (PromptModel) httpSession.getAttribute(PROMPT_MODEL);
        promptModel = promptModel == null ? createPromptModel() : promptModel;
        httpSession.setAttribute(PROMPT_MODEL, promptModel);

        PromptModel.Chat chatElement = new PromptModel.Chat();
        chatElement.setInput(submittedPromptModel.getInput());
        promptModel.getChat().add(chatElement);

        SettingsModel settingsModel = (SettingsModel) httpSession.getAttribute(SETTINGS_MODEL);
        if (settingsModel != null) {
            return chatBotService.sendQuery(promptModel.getChat(), settingsModel);
        } else {
            PromptModel.Chat lastAsError = promptModel.getChat().getLast();
            lastAsError.setInput("-");
            lastAsError.setResult("Api Key in settings is not defined");
            return lastAsError;
        }
    }

    private PromptModel createPromptModel() {
        return new PromptModel();
    }
}