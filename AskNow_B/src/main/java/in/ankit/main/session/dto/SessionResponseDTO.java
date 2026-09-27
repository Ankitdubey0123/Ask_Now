package in.ankit.main.session.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import in.ankit.main.session.model.SessionStatus;

@Data
@Builder
public class SessionResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime scheduledAt;
    private SessionStatus status;
    private String teacherName;
}
