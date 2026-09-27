package in.ankit.main.reels.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReelCommentDto {
    private Long id;
    private String text;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;
}
