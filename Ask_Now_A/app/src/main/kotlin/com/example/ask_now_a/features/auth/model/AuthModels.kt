package com.example.ask_now_a.features.auth.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val id: Int,
    val token: String,
    val role: String,
    val name: String,
    val email: String
)
