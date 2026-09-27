package com.example.ask_now_a.features.chat.repository

import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.chat.model.ChatRequestModel
import com.example.ask_now_a.features.chat.model.SendChatRequestPayload
import com.example.ask_now_a.features.chat.model.UserModel

class ChatRepository {

    private val api = RetrofitClient.apiService

    suspend fun getConnections(userId: Int): Result<List<UserModel>> {
        return try {
            val users = api.getConnections(userId)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchUsers(query: String): Result<List<UserModel>> {
        return try {
            val users = api.searchUsers(query)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendRequest(senderId: Int, receiverId: Int): Result<Unit> {
        return try {
            api.sendChatRequest(SendChatRequestPayload(senderId = senderId, receiverId = receiverId))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRequests(userId: Int): Result<List<ChatRequestModel>> {
        return try {
            val requests = api.getChatRequests(userId)
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptRequest(requestId: Int): Result<Unit> {
        return try {
            api.acceptChatRequest(requestId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectRequest(requestId: Int): Result<Unit> {
        return try {
            api.rejectChatRequest(requestId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
