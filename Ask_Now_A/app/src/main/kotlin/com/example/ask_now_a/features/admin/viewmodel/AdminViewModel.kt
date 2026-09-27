package com.example.ask_now_a.features.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.features.admin.model.AdminStatsModel
import com.example.ask_now_a.features.admin.model.UserManagementItem
import com.example.ask_now_a.features.admin.repository.AdminRepository
import com.example.ask_now_a.features.session.model.SessionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminUiState(
    val isLoading: Boolean = false,
    val stats: AdminStatsModel? = null,
    val users: List<UserManagementItem> = emptyList(),
    val sessions: List<SessionModel> = emptyList(),
    val error: String? = null
)

class AdminViewModel : ViewModel() {

    private val repository = AdminRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        fetchStats()
        fetchUsers()
        fetchSessions()
    }

    fun fetchStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getStats()
            result.onSuccess { stats ->
                _uiState.value = _uiState.value.copy(isLoading = false, stats = stats)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val result = repository.getAllUsers()
            result.onSuccess { users ->
                _uiState.value = _uiState.value.copy(users = users)
            }
        }
    }

    fun fetchSessions() {
        viewModelScope.launch {
            val result = repository.getAllSessions()
            result.onSuccess { sessions ->
                _uiState.value = _uiState.value.copy(sessions = sessions)
            }
        }
    }

    fun toggleUserStatus(userId: Int) {
        viewModelScope.launch {
            repository.toggleUserStatus(userId)
            fetchUsers()
            fetchStats()
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            repository.deleteUser(userId)
            fetchUsers()
            fetchStats()
        }
    }

    fun forceEndSession(sessionId: Int) {
        viewModelScope.launch {
            repository.forceEndSession(sessionId)
            fetchSessions()
            fetchStats()
        }
    }
}
