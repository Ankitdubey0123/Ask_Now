package in.ankit.main.realtime.websockets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ankit.main.realtime.websockets.model.ChatRequest;

public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {
    List<ChatRequest> findByReceiverAndStatus(String receiver, String status);
    List<ChatRequest> findByReceiverIdAndStatus(
            Long receiverId,
            String status
    );
    // ✅ ADD THIS (for duplicate request prevention)
    Optional<ChatRequest> findBySenderIdAndReceiverId(
            Long senderId,
            Long receiverId
    );
}