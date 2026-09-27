package in.ankit.main.realtime.websockets.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    private String sender;
    private String receiver;
    private String content;
}
