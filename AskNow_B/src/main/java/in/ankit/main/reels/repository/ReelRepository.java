package in.ankit.main.reels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ankit.main.auth.entities.User;
import in.ankit.main.reels.model.Reel;

import java.util.List;

@Repository
public interface ReelRepository extends JpaRepository<Reel, Long> {

    List<Reel> findAllByOrderByCreatedAtDesc();

    List<Reel> findByTeacherOrderByCreatedAtDesc(User teacher);
}
