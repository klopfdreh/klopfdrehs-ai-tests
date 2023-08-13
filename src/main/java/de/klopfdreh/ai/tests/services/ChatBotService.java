package de.klopfdreh.ai.tests.services;

import de.klopfdreh.ai.tests.model.PromptModel;
import de.klopfdreh.ai.tests.model.SettingsModel;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ChatBotService {

    private final WebClient.Builder webClientBuilder;

    public ChatBotService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public PromptModel.Chat sendQuery(LinkedList<PromptModel.Chat> chat, SettingsModel settingsModel) {
        JSONObject payload = new JSONObject();

        JSONArray messageList = new JSONArray();

        chat.forEach(chatElement -> {
            JSONObject message = new JSONObject();
            message.put("role", settingsModel.getRole());
            message.put("content", chatElement.getInput());
            messageList.put(message);
        });

        payload.put("model", settingsModel.getModel()); // model is important
        payload.put("messages", messageList);
        payload.put("temperature", settingsModel.getTemperature());

        WebClient webClient = webClientBuilder.baseUrl(settingsModel.getUrl()).build();

        String response = webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(settingsModel.getApiKey()))
                .body(BodyInserters.fromValue(payload.toString()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String resJsonString = new String(Objects.requireNonNull(response).getBytes(), StandardCharsets.UTF_8);
        JSONObject resJson = new JSONObject(resJsonString);

        if (resJson.has("error")) {
            String errorMsg = resJson.getString("error");
            log.error("API error: {}", errorMsg);
        }

        // Parse JSON response
        JSONArray responseArray = resJson.getJSONArray("choices");
        List<String> responseList = new ArrayList<>();

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseObj = responseArray.getJSONObject(i);
            String responseString = responseObj.getJSONObject("message").getString("content");
            responseList.add(responseString);
        }
        // Convert response list to JSON and return it
        PromptModel.Chat lastChatElement = chat.getLast();
        lastChatElement.setResult(String.join(",", responseList));
        return lastChatElement;
    }
}
