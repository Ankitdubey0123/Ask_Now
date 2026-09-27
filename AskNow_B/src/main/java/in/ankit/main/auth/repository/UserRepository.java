package in.ankit.main.auth.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ankit.main.auth.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    long countByRolesContaining(in.ankit.main.auth.entities.Role role);
}