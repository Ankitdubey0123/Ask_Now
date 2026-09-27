package in.ankit.main.realtime.websockets.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
}