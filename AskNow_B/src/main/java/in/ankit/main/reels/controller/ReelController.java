package in.ankit.main.reels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import in.ankit.main.reels.dto.ReelCommentDto;
import in.ankit.main.reels.dto.ReelResponseDto;
import in.ankit.main.reels.service.ReelService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asknow/api/reels")
@RequiredArgsConstructor
public class ReelController {

    private final ReelService reelService;

    // Upload Reel (Teacher only)
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReelResponseDto> uploadReel(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categoryTag", required = false) String categoryTag,
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) {

        ReelResponseDto response = reelService.createReel(title, description, categoryTag, video, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get Reel Feed (Public / Authenticated users)
    @GetMapping("/feed")
    public ResponseEntity<List<ReelResponseDto>> getFeed() {
        return ResponseEntity.ok(reelService.getReelFeed());
    }

    // Get Teacher's own Reels
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/my-reels")
    public ResponseEntity<List<ReelResponseDto>> getMyReels() {
        return ResponseEntity.ok(reelService.getMyReels());
    }

    // Toggle Like on a Reel
    @PostMapping("/{id}/like")
    public ResponseEntity<ReelResponseDto> toggleLike(@PathVariable Long id) {
        return ResponseEntity.ok(reelService.toggleLike(id));
    }

    // ================= REEL COMMENTS =================
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ReelCommentDto>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(reelService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ReelCommentDto> addComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reelService.addComment(id, text));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        reelService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // Increment view count
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementView(@PathVariable Long id) {
        reelService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    // Delete Reel (Teacher owner)
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReel(@PathVariable Long id) {
        reelService.deleteReel(id);
        return ResponseEntity.noContent().build();
    }

    // Media Streaming Endpoint (Video & Thumbnail serving)
    @GetMapping("/stream/{fileName:.+}")
    public ResponseEntity<Resource> streamMedia(@PathVariable String fileName) {
        Resource resource = reelService.loadMediaAsResource(fileName);
        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
