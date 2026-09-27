package in.ankit.main.realtime.websockets.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ChatUserDto {

    private Long userId;
    private String name;

    private String lastMessage;   // optional
    private String lastTime;      // optional
}