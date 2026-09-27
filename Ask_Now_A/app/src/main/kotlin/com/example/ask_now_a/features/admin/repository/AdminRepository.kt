package com.example.ask_now_a.features.admin.repository

import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.admin.model.AdminStatsModel
import com.example.ask_now_a.features.admin.model.UserManagementItem
import com.example.ask_now_a.features.session.model.SessionModel

class AdminRepository {

    private val api = RetrofitClient.apiService

    suspend fun getStats(): Result<AdminStatsModel> {
        return try {
            val res = api.getAdminStats()
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<UserManagementItem>> {
        return try {
            val users = api.getAllUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleUserStatus(userId: Int): Result<Unit> {
        return try {
            api.toggleUserStatus(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return try {
            api.deleteUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllSessions(): Result<List<SessionModel>> {
        return try {
            val sessions = api.getAdminSessions()
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forceEndSession(sessionId: Int): Result<Unit> {
        return try {
            api.forceEndSession(sessionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
