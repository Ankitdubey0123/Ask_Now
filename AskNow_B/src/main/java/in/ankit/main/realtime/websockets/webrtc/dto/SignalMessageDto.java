package in.ankit.main.realtime.websockets.webrtc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalMessageDto {

    public enum SignalType {
        OFFER,
        ANSWER,
        ICE_CANDIDATE,
        JOIN_ROOM,
        LEAVE_ROOM,
        USER_JOINED,
        USER_LEFT
    }

    private SignalType type;
    private String sender;
    private String receiver;
    private Long sessionId;
    private String sdp;
    private String candidate;
    private String sdpMid;
    private Integer sdpMLineIndex;
    private String message;
}
