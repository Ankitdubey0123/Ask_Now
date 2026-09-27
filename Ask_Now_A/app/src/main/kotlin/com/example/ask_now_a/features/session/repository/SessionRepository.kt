package com.example.ask_now_a.features.session.repository

import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.session.model.CreateSessionRequest
import com.example.ask_now_a.features.session.model.SessionModel

class SessionRepository {

    private val api = RetrofitClient.apiService

    suspend fun createSession(title: String, description: String?, scheduledAt: String): Result<SessionModel> {
        return try {
            val response = api.createSession(CreateSessionRequest(title = title, description = description, scheduledAt = scheduledAt))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinSession(sessionId: Int): Result<Unit> {
        return try {
            api.joinSession(sessionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startSession(sessionId: Int): Result<SessionModel> {
        return try {
            val response = api.startSession(sessionId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endSession(sessionId: Int): Result<SessionModel> {
        return try {
            val response = api.endSession(sessionId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLiveSessions(): Result<List<SessionModel>> {
        return try {
            val sessions = api.getAllLiveSessions()
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyCreatedSessions(): Result<List<SessionModel>> {
        return try {
            val sessions = api.getMyCreatedSessions()
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
