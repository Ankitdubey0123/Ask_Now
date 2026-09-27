package in.ankit.main.realtime.websockets.webrtc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import in.ankit.main.realtime.websockets.webrtc.dto.SignalMessageDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebRTCSignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handles peer-to-peer WebRTC signaling (OFFER, ANSWER, ICE_CANDIDATE).
     * If receiver is specified, routes directly to that user queue (/user/queue/signal).
     * Otherwise, broadcasts to the session room topic (/topic/session/{sessionId}/signal).
     */
    @MessageMapping("/webrtc/signal")
    public void processSignal(@Payload SignalMessageDto signal) {
        log.info("Received WebRTC signal of type: {} from: {} for session: {}",
                signal.getType(), signal.getSender(), signal.getSessionId());

        if (signal.getReceiver() != null && !signal.getReceiver().isBlank()) {
            messagingTemplate.convertAndSendToUser(
                    signal.getReceiver(),
                    "/queue/signal",
                    signal
            );
        } else if (signal.getSessionId() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/session/" + signal.getSessionId() + "/signal",
                    signal
            );
        }
    }

    /**
     * Handles room join events for live video classes.
     */
    @MessageMapping("/webrtc/join")
    public void joinRoom(@Payload SignalMessageDto signal) {
        log.info("User {} joined WebRTC live class room for session: {}",
                signal.getSender(), signal.getSessionId());

        signal.setType(SignalMessageDto.SignalType.USER_JOINED);
        messagingTemplate.convertAndSend(
                "/topic/session/" + signal.getSessionId() + "/signal",
                signal
        );
    }

    /**
     * Handles room leave events for live video classes.
     */
    @MessageMapping("/webrtc/leave")
    public void leaveRoom(@Payload SignalMessageDto signal) {
        log.info("User {} left WebRTC live class room for session: {}",
                signal.getSender(), signal.getSessionId());

        signal.setType(SignalMessageDto.SignalType.USER_LEFT);
        messagingTemplate.convertAndSend(
                "/topic/session/" + signal.getSessionId() + "/signal",
                signal
        );
    }
}
