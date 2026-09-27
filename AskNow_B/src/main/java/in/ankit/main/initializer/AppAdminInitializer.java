package in.ankit.main.initializer;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import in.ankit.main.auth.entities.Role;
import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class AppAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;
    
    @Value("${app.teacher.email}")
    private String teacherEmail;
    @Value("${app.teacher.password}")
    private String teacherPassword;



    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = User.builder()
                    .name("Super Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(Role.ADMIN))
                    .enabled(true)
                    .build();
            userRepository.save(admin);

        }
          
            if (!userRepository.existsByEmail(teacherEmail)) {

                User teacher1 = User.builder()
                        .name("Teacher1")
                        .email(teacherEmail)
                        .password(passwordEncoder.encode(teacherPassword))
                        .roles(Set.of(Role.TEACHER))
                        .enabled(true)
                        .build();

                userRepository.save(teacher1);
        }

    }
}
