package in.ankit.main.realtime.websockets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ChatMessageDto {

    private Long senderId;
    private Long receiverId;
    private String message;
    private String timestamp;
}