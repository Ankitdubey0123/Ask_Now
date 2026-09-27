package in.ankit.main.session.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateSessionDto {

    private String title;
    private String description;
    private LocalDateTime scheduledAt;
}
