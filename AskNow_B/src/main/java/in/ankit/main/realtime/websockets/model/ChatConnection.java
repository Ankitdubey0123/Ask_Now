package in.ankit.main.realtime.websockets.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class ChatConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user1;
    private Long user2;
}