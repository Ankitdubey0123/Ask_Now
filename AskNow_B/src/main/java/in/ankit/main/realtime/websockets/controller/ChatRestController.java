package in.ankit.main.realtime.websockets.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import in.ankit.main.realtime.websockets.dto.ChatRequestDto;
import in.ankit.main.realtime.websockets.dto.ChatUserDto;
import in.ankit.main.realtime.websockets.dto.UserDto;
import in.ankit.main.realtime.websockets.services.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/asknow/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

	private final ChatService chatService;

	// 🔎 SEARCH USERS
	@GetMapping("/search")
	public List<UserDto> searchUsers(@RequestParam String query) {
		return chatService.searchUsers(query);
	}

	// 📩 SEND REQUEST
	@PostMapping("/request/send")
	public String sendRequest(@RequestBody Map<String, Long> body) {
		chatService.sendRequest(body.get("senderId"), body.get("receiverId"));
		return "Request Sent";
	}

	// 📥 GET REQUESTS
	@GetMapping("/request/list/{userId}")
	public List<ChatRequestDto> getRequests(@PathVariable Long userId) {
		return chatService.getRequests(userId);
	}

	// ✅ ACCEPT REQUEST
	@PostMapping("/request/accept/{requestId}")
	public String accept(@PathVariable Long requestId) {
		chatService.acceptRequest(requestId);
		return "Accepted";
	}

	// ❌ REJECT REQUEST
	@PostMapping("/request/reject/{requestId}")
	public String reject(@PathVariable Long requestId) {
		chatService.rejectRequest(requestId);
		return "Rejected";
	}

	// 👥 CONNECTIONS
	@GetMapping("/connections/{userId}")
	public List<UserDto> connections(@PathVariable Long userId) {
		return chatService.getConnections(userId);
	}

	@GetMapping("/chat-users/{userId}")
	public List<ChatUserDto> chatUsers(@PathVariable Long userId) {
		return chatService.getChatUsers(userId);
	}
}
