package in.ankit.main.realtime.websockets.services;

import java.util.*;

import org.springframework.stereotype.Service;

import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import in.ankit.main.realtime.websockets.dto.ChatRequestDto;
import in.ankit.main.realtime.websockets.dto.ChatUserDto;
import in.ankit.main.realtime.websockets.dto.UserDto;
import in.ankit.main.realtime.websockets.model.ChatConnection;
import in.ankit.main.realtime.websockets.model.ChatRequest;
import in.ankit.main.realtime.websockets.repository.ChatConnectionRepository;
import in.ankit.main.realtime.websockets.repository.ChatRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepo;
    private final ChatRequestRepository requestRepo;
    private final ChatConnectionRepository connectionRepo;

    // 🔎 SEARCH USERS
    public List<UserDto> searchUsers(String query) {

        List<User> users =
                userRepo.findByNameContainingIgnoreCase(query);

        return users.stream()
                .map(u -> new UserDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail()
                ))
                .toList();
    }

    // 📩 SEND REQUEST
    public void sendRequest(Long senderId, Long receiverId) {

        Optional<ChatRequest> existing =
                requestRepo.findBySenderIdAndReceiverId(senderId, receiverId);

        if (existing.isPresent()) {
            return; // already sent
        }

        ChatRequest request = new ChatRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus("PENDING");

        requestRepo.save(request);
    }

    // 📥 GET REQUESTS
    public List<ChatRequestDto> getRequests(Long userId) {

        List<ChatRequest> requests =
                requestRepo.findByReceiverIdAndStatus(userId, "PENDING");

        List<ChatRequestDto> result = new ArrayList<>();

        for (ChatRequest req : requests) {

            User sender =
                    userRepo.findById(req.getSenderId()).orElse(null);

            result.add(new ChatRequestDto(
                    req.getId(),
                    req.getSenderId(),
                    sender != null ? sender.getName() : "Unknown"
            ));
        }

        return result;
    }

    // ✅ ACCEPT REQUEST
    public void acceptRequest(Long requestId) {

        ChatRequest req =
                requestRepo.findById(requestId).orElseThrow();

        req.setStatus("ACCEPTED");
        requestRepo.save(req);

        ChatConnection connection = new ChatConnection();
        connection.setUser1(req.getSenderId());
        connection.setUser2(req.getReceiverId());

        connectionRepo.save(connection);
    }

    // ❌ REJECT REQUEST
    public void rejectRequest(Long requestId) {
        ChatRequest req = requestRepo.findById(requestId).orElseThrow();
        req.setStatus("REJECTED");
        requestRepo.save(req);
    }

    // 👥 GET CONNECTIONS
    public List<UserDto> getConnections(Long userId) {

        List<ChatConnection> connections =
                connectionRepo.findByUser1OrUser2(userId, userId);

        List<UserDto> result = new ArrayList<>();

        for (ChatConnection c : connections) {

            Long otherUserId =
                    c.getUser1().equals(userId)
                            ? c.getUser2()
                            : c.getUser1();

            User user = userRepo.findById(otherUserId).orElse(null);

            if (user != null) {
                result.add(new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ));
            }
        }

        return result;
    }

    // 💬 GET CHAT USERS (CHAT LIST)
    public List<ChatUserDto> getChatUsers(Long userId) {

        List<ChatConnection> connections =
                connectionRepo.findByUser1OrUser2(userId, userId);

        List<ChatUserDto> result = new ArrayList<>();

        for (ChatConnection c : connections) {

            Long otherUserId =
                    c.getUser1().equals(userId)
                            ? c.getUser2()
                            : c.getUser1();

            User user = userRepo.findById(otherUserId).orElse(null);

            if (user != null) {
                String lastMessage = "No messages yet";
                String lastTime = "";

                result.add(new ChatUserDto(
                        user.getId(),
                        user.getName(),
                        lastMessage,
                        lastTime
                ));
            }
        }

        return result;
    }
}
