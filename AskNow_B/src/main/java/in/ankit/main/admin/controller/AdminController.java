package in.ankit.main.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import in.ankit.main.admin.dto.AdminDashboardStatsDto;
import in.ankit.main.admin.dto.UserAdminDto;
import in.ankit.main.admin.service.AdminService;
import in.ankit.main.auth.entities.Role;
import in.ankit.main.reels.dto.ReelResponseDto;
import in.ankit.main.session.dto.SessionResponseDTO;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/asknow/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // Platform Statistics & Overview Dashboard
    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // ================= USER MANAGEMENT =================
    @GetMapping("/users")
    public ResponseEntity<List<UserAdminDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PatchMapping("/users/{userId}/toggle-status")
    public ResponseEntity<UserAdminDto> toggleUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.toggleUserStatus(userId));
    }

    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<UserAdminDto> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody Set<Role> roles) {
        return ResponseEntity.ok(adminService.updateUserRoles(userId, roles));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ================= SESSION MODERATION =================
    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponseDTO>> getAllSessions() {
        return ResponseEntity.ok(adminService.getAllSessions());
    }

    @PatchMapping("/sessions/{sessionId}/end")
    public ResponseEntity<SessionResponseDTO> forceEndSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(adminService.forceEndSession(sessionId));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        adminService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    // ================= REEL MODERATION =================
    @GetMapping("/reels")
    public ResponseEntity<List<ReelResponseDto>> getAllReels() {
        return ResponseEntity.ok(adminService.getAllReels());
    }

    @DeleteMapping("/reels/{reelId}")
    public ResponseEntity<Void> deleteReel(@PathVariable Long reelId) {
        adminService.deleteReel(reelId);
        return ResponseEntity.noContent().build();
    }
}
