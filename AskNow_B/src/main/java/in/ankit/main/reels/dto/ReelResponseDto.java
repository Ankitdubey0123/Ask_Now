package in.ankit.main.reels.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReelResponseDto {

    private Long id;
    private String title;
    private String description;
    private String categoryTag;
    private String videoUrl;
    private String thumbnailUrl;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private int likesCount;
    private int commentsCount;
    private Long viewsCount;
    private boolean likedByCurrentUser;
    private LocalDateTime createdAt;
}
