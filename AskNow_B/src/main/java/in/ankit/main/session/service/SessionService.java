package in.ankit.main.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import in.ankit.main.session.dto.CreateSessionDto;
import in.ankit.main.session.dto.SessionResponseDTO;
import in.ankit.main.session.model.Session;
import in.ankit.main.session.model.SessionStatus;
import in.ankit.main.session.repository.SessionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    // ================= CREATE SESSION =================

    public SessionResponseDTO createSession(CreateSessionDto request) {

        User teacher = getCurrentUser();

        Session session = Session.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledAt(request.getScheduledAt())
                .status(SessionStatus.SCHEDULED)
                .teacher(teacher)
                .build();

        sessionRepository.save(session);

        return mapToResponse(session);
    }

    // ================= JOIN SESSION =================

    public SessionResponseDTO joinSession(Long sessionId) {

        User student = getCurrentUser();

        Session session = getSessionOrThrow(sessionId);

        if (session.getStatus() != SessionStatus.LIVE) {
            throw new IllegalStateException("Session is not live");
        }

        if (session.getStudents().contains(student)) {
            throw new IllegalStateException("Already joined this session");
        }

        session.getStudents().add(student);

        return mapToResponse(session);
    }

    // ================= START SESSION =================

    public SessionResponseDTO startSession(Long sessionId) {

        User teacher = getCurrentUser();

        Session session = getSessionOrThrow(sessionId);

        validateTeacherOwnership(session, teacher);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new IllegalStateException("Session cannot be started");
        }

        session.setStatus(SessionStatus.LIVE);

        return mapToResponse(session);
    }

    // ================= END SESSION =================

    public SessionResponseDTO endSession(Long sessionId) {

        User teacher = getCurrentUser();

        Session session = getSessionOrThrow(sessionId);

        validateTeacherOwnership(session, teacher);

        if (session.getStatus() != SessionStatus.LIVE) {
            throw new IllegalStateException("Session is not live");
        }

        session.setStatus(SessionStatus.ENDED);

        return mapToResponse(session);
    }

    // ================= ALL LIVE =================

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getAllLiveSessions() {

        return sessionRepository.findByStatus(SessionStatus.LIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= MY CREATED =================

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getMyCreatedSessions() {

        User teacher = getCurrentUser();

        return sessionRepository.findByTeacher(teacher)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= MY JOINED =================

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getMyJoinedSessions() {

        User student = getCurrentUser();

        return sessionRepository.findByStudentsContaining(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= HELPERS =================

    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private Session getSessionOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Session not found"));
    }

    private void validateTeacherOwnership(Session session, User teacher) {

        if (!session.getTeacher().getId().equals(teacher.getId())) {
            throw new IllegalStateException("You are not allowed to modify this session");
        }
    }

    private SessionResponseDTO mapToResponse(Session session) {

        return SessionResponseDTO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .scheduledAt(session.getScheduledAt())
                .status(session.getStatus())
                .teacherName(session.getTeacher().getName())
                .build();
    }
}
