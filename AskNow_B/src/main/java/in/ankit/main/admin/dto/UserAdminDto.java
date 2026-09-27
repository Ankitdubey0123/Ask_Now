package in.ankit.main.admin.dto;

import lombok.*;
import in.ankit.main.auth.entities.Role;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminDto {

    private Long id;
    private String email;
    private String name;
    private Set<Role> roles;
    private boolean enabled;
}
