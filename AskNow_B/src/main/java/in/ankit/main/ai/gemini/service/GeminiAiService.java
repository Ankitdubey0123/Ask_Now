package in.ankit.main.ai.gemini.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import in.ankit.main.ai.gemini.dto.AiDoubtRequestDto;
import in.ankit.main.ai.gemini.dto.AiResponseDto;

import java.util.*;

@Service
@Slf4j
public class GeminiAiService {

    @Value("${gemini.api.key:YOUR_GEMINI_API_KEY}")
    private String apiKey;

    // Not final — initialized as a plain instance field (not Spring-managed)
    private final RestClient restClient = RestClient.create("https://generativelanguage.googleapis.com");

    /**
     * Solves student doubts using Gemini Flash AI with Multimodal OCR support for images.
     */
    public AiResponseDto solveDoubt(AiDoubtRequestDto request) {
        String prompt = "You are AskNow AI Smart Tutor, a friendly expert teacher. "
                + "Provide clear, step-by-step educational explanations for the student's question/image. Question: "
                + (request.getQuestion() != null ? request.getQuestion() : "Please analyze the attached image/notes and explain step-by-step.");

        String geminiResponse = callGeminiApi(prompt, request.getBase64Image(), request.getMimeType());
        return AiResponseDto.builder()
                .result(geminiResponse)
                .action("ASK_DOUBT")
                .build();
    }

    /**
     * Generates a concise summary of class notes or short video reels.
     */
    public AiResponseDto summarizeText(String text) {
        String prompt = "Summarize the following educational content/notes into key takeaways for students:\n\n" + text;
        String result = callGeminiApi(prompt, null, null);
        return AiResponseDto.builder()
                .result(result)
                .action("SUMMARIZE")
                .build();
    }

    /**
     * Generates multiple-choice quiz questions for student practice.
     */
    public AiResponseDto generateQuiz(String topic) {
        String prompt = "Generate 3 multiple-choice questions (with options A, B, C, D and the correct answer explained) for topic: " + topic;
        String result = callGeminiApi(prompt, null, null);
        return AiResponseDto.builder()
                .result(result)
                .action("QUIZ")
                .build();
    }

    // ================= GEMINI REST API INTEGRATION =================
    private String callGeminiApi(String textPrompt, String base64Image, String mimeType) {
        try {
            List<Map<String, Object>> parts = new ArrayList<>();

            // Add text prompt
            parts.add(Map.of("text", textPrompt));

            // Add image part if provided (OCR / Multimodal)
            if (base64Image != null && !base64Image.isBlank()) {
                parts.add(Map.of(
                        "inline_data", Map.of(
                                "mime_type", (mimeType != null && !mimeType.isBlank()) ? mimeType : "image/jpeg",
                                "data", base64Image
                        )
                ));
            }

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", parts)
                    )
            );

            @SuppressWarnings("rawtypes")
            Map response = restClient.post()
                    .uri("/v1beta/models/gemini-1.5-flash:generateContent?key={key}", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            return extractTextFromGeminiResponse(response);
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            return "Unable to connect to Gemini AI service. Please ensure your Gemini API Key is configured in application.properties.";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromGeminiResponse(@SuppressWarnings("rawtypes") Map response) {
        try {
            if (response == null || !response.containsKey("candidates")) {
                return "No response received from Gemini AI.";
            }
            List candidates = (List) response.get("candidates");
            if (candidates.isEmpty()) return "No content generated.";

            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            return (String) firstPart.get("text");
        } catch (Exception e) {
            log.error("Failed to parse Gemini response payload", e);
            return "Response format error from Gemini API.";
        }
    }
}
