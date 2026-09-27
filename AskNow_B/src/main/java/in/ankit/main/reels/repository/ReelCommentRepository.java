package in.ankit.main.reels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ankit.main.reels.model.Reel;
import in.ankit.main.reels.model.ReelComment;

import java.util.List;

@Repository
public interface ReelCommentRepository extends JpaRepository<ReelComment, Long> {
    List<ReelComment> findByReelOrderByCreatedAtDesc(Reel reel);
    long countByReel(Reel reel);
}
