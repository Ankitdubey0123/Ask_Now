package in.ankit.main.ai.gemini.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiDoubtRequestDto {
    private String question;
    private String base64Image; // Optional base64 encoded image for OCR & multimodal analysis
    private String mimeType;    // e.g. "image/jpeg", "image/png"
}
