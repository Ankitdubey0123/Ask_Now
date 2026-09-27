package com.example.ask_now_a.features.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.core.storage.LocalStorage
import com.example.ask_now_a.features.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val role: String? = null,
    val userId: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val localStorage = LocalStorage(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAutoLogin()
    }

    fun checkAutoLogin() {
        viewModelScope.launch {
            val token = localStorage.getToken()
            val role = localStorage.getRole()
            val userId = localStorage.getUserId()
            val name = localStorage.getName()
            val email = localStorage.getEmail()

            if (!token.isNullOrBlank()) {
                RetrofitClient.setToken(token)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    token = token,
                    role = role,
                    userId = userId,
                    name = name,
                    email = email
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.login(email.trim(), password.trim())

            result.onSuccess { response ->
                RetrofitClient.setToken(response.token)
                localStorage.saveUser(
                    id = response.id,
                    token = response.token,
                    role = response.role,
                    name = response.name,
                    email = response.email
                )

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = true,
                    token = response.token,
                    role = response.role,
                    userId = response.id,
                    name = response.name,
                    email = response.email
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Authentication failed"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "All fields are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.register(name.trim(), email.trim(), password.trim(), role)

            result.onSuccess { response ->
                RetrofitClient.setToken(response.token)
                localStorage.saveUser(
                    id = response.id,
                    token = response.token,
                    role = response.role,
                    name = response.name,
                    email = response.email
                )

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = true,
                    token = response.token,
                    role = response.role,
                    userId = response.id,
                    name = response.name,
                    email = response.email
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Registration failed"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            localStorage.clear()
            RetrofitClient.setToken("")
            _uiState.value = AuthUiState()
        }
    }
}
