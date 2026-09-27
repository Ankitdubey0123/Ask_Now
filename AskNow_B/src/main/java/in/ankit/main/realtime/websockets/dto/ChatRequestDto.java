package in.ankit.main.realtime.websockets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ChatRequestDto {

    private Long id;
    private Long senderId;
    private String senderName;
}