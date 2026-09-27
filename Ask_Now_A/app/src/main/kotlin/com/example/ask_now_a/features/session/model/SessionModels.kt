package com.example.ask_now_a.features.session.model

data class CreateSessionRequest(
    val title: String,
    val description: String? = null,
    val scheduledAt: String
)

data class SessionModel(
    val id: Int,
    val title: String,
    val description: String? = null,
    val scheduledAt: String,
    val status: String = "SCHEDULED", // SCHEDULED, LIVE, ENDED
    val teacherName: String? = null
)
