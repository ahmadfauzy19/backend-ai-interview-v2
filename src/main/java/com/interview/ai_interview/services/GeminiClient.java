package com.interview.ai_interview.services;

import org.springframework.stereotype.Service;

import com.interview.ai_interview.configs.GeminiConfig;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiClient {

    private final Client client;
    private final String model;

    public GeminiClient(GeminiConfig props) {
        System.setProperty("GEMINI_API_KEY", props.getApiKey());

        this.model = props.getModel();

        this.client = Client.builder()
                .apiKey(props.getApiKey()) 
                .build();
    }


    /**
     * Kirim prompt ke Gemini dan ambil text response
     */
    public String evaluate(String systemPrompt, String userPrompt) {

        String finalPrompt = systemPrompt + "\n\n" + userPrompt;

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        finalPrompt,
                        null
                );

        return response.text();
    }
}

