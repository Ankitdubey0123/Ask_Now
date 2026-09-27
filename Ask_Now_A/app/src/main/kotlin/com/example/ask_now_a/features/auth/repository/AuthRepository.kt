package com.example.ask_now_a.features.auth.repository

import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.auth.model.AuthResponse
import com.example.ask_now_a.features.auth.model.LoginRequest
import com.example.ask_now_a.features.auth.model.RegisterRequest

class AuthRepository {

    private val api = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email = email, password = password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Login failed"))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(name = name, email = email, password = password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Registration failed"))
        }
    }
}
