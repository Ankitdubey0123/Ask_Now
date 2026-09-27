package in.ankit.main.session.controller;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import in.ankit.main.session.dto.CreateSessionDto;
import in.ankit.main.session.dto.SessionResponseDTO;
import in.ankit.main.session.service.SessionService;

@RestController
@RequestMapping("/asknow/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // Only TEACHER can create
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public SessionResponseDTO createSession(@RequestBody CreateSessionDto request) {
        return sessionService.createSession(request);
    }

    // Only STUDENT can join
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/join/{id}")
    public void joinSession(@PathVariable Long id) {
        sessionService.joinSession(id);
    }
    
    @PatchMapping("/start/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public SessionResponseDTO startSession(@PathVariable Long id) {
        return sessionService.startSession(id);
    }

    @PatchMapping("/end/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public SessionResponseDTO endSession(@PathVariable Long id) {
        return sessionService.endSession(id);
    }
    
    // API for getting sessions data in frontend
    
    @GetMapping("/all-live")
    public List<SessionResponseDTO> getAllLiveSessions() {
        return sessionService.getAllLiveSessions();
    }


    @GetMapping("/my-created")
    @PreAuthorize("hasRole('TEACHER')")
    public List<SessionResponseDTO> getMyCreatedSessions() {
        return sessionService.getMyCreatedSessions();
    }


    @GetMapping("/my-joined")
    @PreAuthorize("hasRole('STUDENT')")
    public List<SessionResponseDTO> getMyJoinedSessions() {
        return sessionService.getMyJoinedSessions();
    }


    
}
