package de.klopfdreh.ai.tests.model;

import lombok.Data;

import java.util.LinkedList;

@Data
public class PromptModel {

    public static final String PROMPT_MODEL = "promptModel";

    public String input;

    public LinkedList<Chat> chat = new LinkedList<>();

    @Data
    public static class Chat {

        private String input;

        private String result;
    }
}
