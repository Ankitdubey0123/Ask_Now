package com.example.ask_now_a.features.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.features.chat.model.ChatMessage
import com.example.ask_now_a.features.chat.model.ChatRequestModel
import com.example.ask_now_a.features.chat.model.UserModel
import com.example.ask_now_a.features.chat.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val connections: List<UserModel> = emptyList(),
    val searchResults: List<UserModel> = emptyList(),
    val incomingRequests: List<ChatRequestModel> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val error: String? = null
)

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun loadConnections(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getConnections(userId)
            result.onSuccess { users ->
                _uiState.value = _uiState.value.copy(isLoading = false, connections = users)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.searchUsers(query.trim())
            result.onSuccess { users ->
                _uiState.value = _uiState.value.copy(isLoading = false, searchResults = users)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun loadRequests(userId: Int) {
        viewModelScope.launch {
            val result = repository.getRequests(userId)
            result.onSuccess { requests ->
                _uiState.value = _uiState.value.copy(incomingRequests = requests)
            }
        }
    }

    fun sendRequest(senderId: Int, receiverId: Int) {
        viewModelScope.launch {
            repository.sendRequest(senderId, receiverId)
        }
    }

    fun acceptRequest(requestId: Int, userId: Int) {
        viewModelScope.launch {
            repository.acceptRequest(requestId)
            loadRequests(userId)
            loadConnections(userId)
        }
    }

    fun rejectRequest(requestId: Int, userId: Int) {
        viewModelScope.launch {
            repository.rejectRequest(requestId)
            loadRequests(userId)
        }
    }

    fun sendMessage(senderId: Int, receiverId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(sender = senderId.toString(), receiver = receiverId, message = text.trim())
        val updated = _uiState.value.messages + newMsg
        _uiState.value = _uiState.value.copy(messages = updated)
    }
}
