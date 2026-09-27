package in.ankit.main.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ankit.main.auth.entities.User;
import in.ankit.main.session.model.Session;
import in.ankit.main.session.model.SessionStatus;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByStatus(SessionStatus status);

    List<Session> findByTeacher(User teacher);

    List<Session> findByStudentsContaining(User student);

    long countByStatus(SessionStatus status);
}
