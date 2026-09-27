package in.ankit.main.reels.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import in.ankit.main.reels.dto.ReelCommentDto;
import in.ankit.main.reels.dto.ReelResponseDto;
import in.ankit.main.reels.model.Reel;
import in.ankit.main.reels.model.ReelComment;
import in.ankit.main.reels.repository.ReelCommentRepository;
import in.ankit.main.reels.repository.ReelRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReelService {

    private final ReelRepository reelRepository;
    private final ReelCommentRepository commentRepository;
    private final UserRepository userRepository;

    private final Path uploadDir = Paths.get("uploads/reels").toAbsolutePath().normalize();

    private void initStorage() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            log.error("Could not create upload directory", e);
            throw new RuntimeException("Failed to initialize file storage directory", e);
        }
    }

    // ================= UPLOAD REEL =================
    public ReelResponseDto createReel(String title, String description, String categoryTag, MultipartFile videoFile, MultipartFile thumbnailFile) {
        initStorage();
        User teacher = getCurrentUser();

        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is required");
        }

        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        Path targetVideoPath = uploadDir.resolve(videoFileName);

        try {
            Files.copy(videoFile.getInputStream(), targetVideoPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to store video file", e);
            throw new RuntimeException("Failed to store video file", e);
        }

        String thumbnailFileName = null;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            thumbnailFileName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();
            Path targetThumbPath = uploadDir.resolve(thumbnailFileName);
            try {
                Files.copy(thumbnailFile.getInputStream(), targetThumbPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.warn("Failed to store thumbnail file", e);
            }
        }

        String videoUrl = "/asknow/api/reels/stream/" + videoFileName;
        String thumbnailUrl = (thumbnailFileName != null) ? "/asknow/api/reels/stream/" + thumbnailFileName : null;

        Reel reel = Reel.builder()
                .title(title)
                .description(description)
                .categoryTag((categoryTag != null && !categoryTag.isBlank()) ? categoryTag : "#ProblemSolving")
                .videoUrl(videoUrl)
                .thumbnailUrl(thumbnailUrl)
                .teacher(teacher)
                .viewsCount(0L)
                .build();

        reelRepository.save(reel);
        return mapToResponse(reel, teacher);
    }

    // ================= GET REEL FEED =================
    @Transactional(readOnly = true)
    public List<ReelResponseDto> getReelFeed() {
        User currentUser = getCurrentUserOrNull();
        return reelRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(reel -> mapToResponse(reel, currentUser))
                .toList();
    }

    // ================= GET TEACHER REELS =================
    @Transactional(readOnly = true)
    public List<ReelResponseDto> getMyReels() {
        User teacher = getCurrentUser();
        return reelRepository.findByTeacherOrderByCreatedAtDesc(teacher)
                .stream()
                .map(reel -> mapToResponse(reel, teacher))
                .toList();
    }

    // ================= TOGGLE LIKE =================
    public ReelResponseDto toggleLike(Long reelId) {
        User currentUser = getCurrentUser();
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));

        if (reel.getLikedByUsers().contains(currentUser)) {
            reel.getLikedByUsers().remove(currentUser);
        } else {
            reel.getLikedByUsers().add(currentUser);
        }

        return mapToResponse(reel, currentUser);
    }

    // ================= COMMENTS FUNCTIONALITY =================
    @Transactional(readOnly = true)
    public List<ReelCommentDto> getComments(Long reelId) {
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));
        return commentRepository.findByReelOrderByCreatedAtDesc(reel)
                .stream()
                .map(c -> ReelCommentDto.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .userId(c.getUser().getId())
                        .userName(c.getUser().getName())
                        .userEmail(c.getUser().getEmail())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();
    }

    public ReelCommentDto addComment(Long reelId, String text) {
        User user = getCurrentUser();
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));

        ReelComment comment = ReelComment.builder()
                .text(text)
                .user(user)
                .reel(reel)
                .build();

        commentRepository.save(comment);

        return ReelCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public void deleteComment(Long commentId) {
        User user = getCurrentUser();
        ReelComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    // ================= INCREMENT VIEW =================
    public void incrementViewCount(Long reelId) {
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));
        reel.setViewsCount(reel.getViewsCount() + 1);
    }

    // ================= LOAD MEDIA RESOURCE FOR STREAMING =================
    @Transactional(readOnly = true)
    public Resource loadMediaAsResource(String fileName) {
        try {
            Path filePath = uploadDir.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    // ================= DELETE REEL =================
    public void deleteReel(Long reelId) {
        User user = getCurrentUser();
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));

        if (!reel.getTeacher().getId().equals(user.getId())) {
            throw new IllegalStateException("Not authorized to delete this reel");
        }

        reelRepository.delete(reel);
    }

    // ================= HELPERS =================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private User getCurrentUserOrNull() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private ReelResponseDto mapToResponse(Reel reel, User currentUser) {
        boolean liked = currentUser != null && reel.getLikedByUsers().contains(currentUser);
        int commentsCount = (int) commentRepository.countByReel(reel);

        return ReelResponseDto.builder()
                .id(reel.getId())
                .title(reel.getTitle())
                .description(reel.getDescription())
                .categoryTag(reel.getCategoryTag() != null ? reel.getCategoryTag() : "#ProblemSolving")
                .videoUrl(reel.getVideoUrl())
                .thumbnailUrl(reel.getThumbnailUrl())
                .teacherId(reel.getTeacher().getId())
                .teacherName(reel.getTeacher().getName())
                .teacherEmail(reel.getTeacher().getEmail())
                .likesCount(reel.getLikedByUsers().size())
                .commentsCount(commentsCount)
                .viewsCount(reel.getViewsCount())
                .likedByCurrentUser(liked)
                .createdAt(reel.getCreatedAt())
                .build();
    }
}
