package com.SmartmailAssistant.Service;

import com.SmartmailAssistant.dto.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGenaratorService {

    private static final Logger logger = LoggerFactory.getLogger(EmailGenaratorService.class);

    @Value("${gemini.api.url}")
    private String getgeminiApiUrl;

    @Value("${gemini.api.key}")
    private String getGeminiApiKey;

    private final WebClient webClient;

    public EmailGenaratorService(WebClient.Builder webClientBuilder){
        this.webClient = WebClient.builder()
                .build();
    }


    public String generateEmailReply(EmailRequest emailRequest){
        // Build The Prompt
        String prompt = buildPrompt(emailRequest);

        // correct req format
        Map<String , Object > requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                               Map.of( "text",prompt)
                        })
                }
        );

        logger.info("Sending request to Google Gemini API");

        // Do ReQuest
        String response = webClient.post()
                .uri(getgeminiApiUrl+getGeminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // extract and return Response
        String extractedResponse = extractResponseContent(response);
        logger.info("Received response: {}", extractedResponse);
        return extractedResponse;
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
           return "request Processing Error" + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate the Professional email reply for the following email content, Please Don't Generate the Subject line ");
        if(emailRequest.getTypeOfEmail() != null && !emailRequest.getTypeOfEmail().isEmpty()){
            prompt.append("use a ").append(emailRequest.getTypeOfEmail()).append("tone..");
        }
        prompt.append("\n Orignal Email is : ").append(emailRequest.getOriginalEmailContent());
        return prompt.toString();
    }
}
