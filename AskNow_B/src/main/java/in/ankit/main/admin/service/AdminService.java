package in.ankit.main.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ankit.main.admin.dto.AdminDashboardStatsDto;
import in.ankit.main.admin.dto.UserAdminDto;
import in.ankit.main.auth.entities.Role;
import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import in.ankit.main.reels.dto.ReelResponseDto;
import in.ankit.main.reels.model.Reel;
import in.ankit.main.reels.repository.ReelRepository;
import in.ankit.main.reels.service.ReelService;
import in.ankit.main.session.dto.SessionResponseDTO;
import in.ankit.main.session.model.Session;
import in.ankit.main.session.model.SessionStatus;
import in.ankit.main.session.repository.SessionRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ReelRepository reelRepository;
    private final ReelService reelService;

    // ================= PLATFORM DASHBOARD STATS =================
    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalStudents = userRepository.countByRolesContaining(Role.STUDENT);
        long totalTeachers = userRepository.countByRolesContaining(Role.TEACHER);
        long totalAdmins = userRepository.countByRolesContaining(Role.ADMIN);

        long totalSessions = sessionRepository.count();
        long totalLiveSessions = sessionRepository.countByStatus(SessionStatus.LIVE);
        long totalCompletedSessions = sessionRepository.countByStatus(SessionStatus.ENDED);

        long totalReels = reelRepository.count();
        long totalReelViews = reelRepository.findAll().stream()
                .mapToLong(Reel::getViewsCount)
                .sum();

        return AdminDashboardStatsDto.builder()
                .totalUsers(totalUsers)
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalAdmins(totalAdmins)
                .totalSessions(totalSessions)
                .totalLiveSessions(totalLiveSessions)
                .totalCompletedSessions(totalCompletedSessions)
                .totalReels(totalReels)
                .totalReelViews(totalReelViews)
                .build();
    }

    // ================= USER MANAGEMENT =================
    @Transactional(readOnly = true)
    public List<UserAdminDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToDto)
                .toList();
    }

    public UserAdminDto toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        log.info("Admin updated status for user ID {}: enabled={}", userId, user.isEnabled());
        return mapUserToDto(user);
    }

    public UserAdminDto updateUserRoles(Long userId, Set<Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        user.setRoles(roles);
        userRepository.save(user);
        log.info("Admin updated roles for user ID {}: roles={}", userId, roles);
        return mapUserToDto(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        userRepository.delete(user);
        log.info("Admin deleted user ID {}", userId);
    }

    // ================= SESSION MODERATION =================
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(this::mapSessionToDto)
                .toList();
    }

    public SessionResponseDTO forceEndSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalStateException("Session not found"));
        session.setStatus(SessionStatus.ENDED);
        log.info("Admin force-ended session ID {}", sessionId);
        return mapSessionToDto(session);
    }

    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalStateException("Session not found"));
        sessionRepository.delete(session);
        log.info("Admin deleted session ID {}", sessionId);
    }

    // ================= REEL MODERATION =================
    @Transactional(readOnly = true)
    public List<ReelResponseDto> getAllReels() {
        return reelService.getReelFeed();
    }

    public void deleteReel(Long reelId) {
        Reel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new IllegalStateException("Reel not found"));
        reelRepository.delete(reel);
        log.info("Admin moderated/deleted reel ID {}", reelId);
    }

    // ================= HELPERS =================
    private UserAdminDto mapUserToDto(User user) {
        return UserAdminDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .build();
    }

    private SessionResponseDTO mapSessionToDto(Session session) {
        return SessionResponseDTO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .scheduledAt(session.getScheduledAt())
                .status(session.getStatus())
                .teacherName(session.getTeacher() != null ? session.getTeacher().getName() : "N/A")
                .build();
    }
}
