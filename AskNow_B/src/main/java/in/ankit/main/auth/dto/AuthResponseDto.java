package in.ankit.main.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private Long id;        // ✅ ADD THIS
    private String token;
    private String email;
    private String name;
    private String role;
}
