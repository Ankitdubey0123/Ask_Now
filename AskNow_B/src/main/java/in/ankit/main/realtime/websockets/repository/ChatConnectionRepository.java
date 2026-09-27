package in.ankit.main.realtime.websockets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ankit.main.realtime.websockets.model.ChatConnection;

public interface ChatConnectionRepository extends JpaRepository<ChatConnection, Long> {

    @Query("SELECT c FROM ChatConnection c WHERE c.user1 = :user OR c.user2 = :user")
    List<ChatConnection> findConnections(String user);
    List<ChatConnection> findByUser1OrUser2(
            Long user1,
            Long user2
    );
}