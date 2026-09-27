package in.ankit.main.ai.gemini.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponseDto {
    private String result;
    private String action;
}
