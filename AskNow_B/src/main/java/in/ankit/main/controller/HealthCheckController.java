package in.ankit.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> rootHealthCheck() {
        return getHealthResponse();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return getHealthResponse();
    }

    private ResponseEntity<Map<String, String>> getHealthResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "AskNow Backend is running successfully");
        return ResponseEntity.ok(response);
    }
}
