package com.example.ask_now_a.features.session.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.features.session.model.SessionModel
import com.example.ask_now_a.features.session.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = false,
    val liveSessions: List<SessionModel> = emptyList(),
    val myCreatedSessions: List<SessionModel> = emptyList(),
    val createSuccess: Boolean = false,
    val error: String? = null
)

class SessionViewModel : ViewModel() {

    private val repository = SessionRepository()

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        fetchLiveSessions()
        fetchMyCreatedSessions()
    }

    fun fetchLiveSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getLiveSessions()
            result.onSuccess { sessions ->
                _uiState.value = _uiState.value.copy(isLoading = false, liveSessions = sessions)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun fetchMyCreatedSessions() {
        viewModelScope.launch {
            val result = repository.getMyCreatedSessions()
            result.onSuccess { sessions ->
                _uiState.value = _uiState.value.copy(myCreatedSessions = sessions)
            }
        }
    }

    fun createSession(title: String, description: String?, scheduledAt: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, createSuccess = false)
            val result = repository.createSession(title, description, scheduledAt)
            result.onSuccess { session ->
                val updated = listOf(session) + _uiState.value.myCreatedSessions
                _uiState.value = _uiState.value.copy(isLoading = false, myCreatedSessions = updated, createSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to schedule session: ${e.message}")
            }
        }
    }

    fun startSession(sessionId: Int) {
        viewModelScope.launch {
            repository.startSession(sessionId)
            fetchMyCreatedSessions()
            fetchLiveSessions()
        }
    }

    fun endSession(sessionId: Int) {
        viewModelScope.launch {
            repository.endSession(sessionId)
            fetchMyCreatedSessions()
            fetchLiveSessions()
        }
    }
}
