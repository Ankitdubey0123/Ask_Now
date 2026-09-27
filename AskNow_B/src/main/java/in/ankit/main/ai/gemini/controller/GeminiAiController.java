package in.ankit.main.ai.gemini.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.ankit.main.ai.gemini.dto.AiDoubtRequestDto;
import in.ankit.main.ai.gemini.dto.AiResponseDto;
import in.ankit.main.ai.gemini.service.GeminiAiService;

@RestController
@RequestMapping("/asknow/api/ai")
@RequiredArgsConstructor
public class GeminiAiController {

    private final GeminiAiService geminiAiService;

    // AI Smart Tutor: Solves doubt questions or image notes (OCR)
    @PostMapping("/ask-doubt")
    public ResponseEntity<AiResponseDto> askDoubt(@RequestBody AiDoubtRequestDto request) {
        return ResponseEntity.ok(geminiAiService.solveDoubt(request));
    }

    // AI Summarizer for notes & reel topics
    @PostMapping("/summarize")
    public ResponseEntity<AiResponseDto> summarize(@RequestBody String text) {
        return ResponseEntity.ok(geminiAiService.summarizeText(text));
    }

    // AI Quiz Generator
    @PostMapping("/generate-quiz")
    public ResponseEntity<AiResponseDto> generateQuiz(@RequestParam("topic") String topic) {
        return ResponseEntity.ok(geminiAiService.generateQuiz(topic));
    }
}
