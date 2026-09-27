package com.example.ask_now_a.features.ai_tutor.repository

import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.ai_tutor.model.AiDoubtRequest
import com.example.ask_now_a.features.ai_tutor.model.AiResponseModel

class AiRepository {

    private val api = RetrofitClient.apiService

    suspend fun askDoubt(question: String?, base64Image: String?, mimeType: String?): Result<AiResponseModel> {
        return try {
            val response = api.askDoubt(AiDoubtRequest(question = question, base64Image = base64Image, mimeType = mimeType ?: "image/jpeg"))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun summarizeText(text: String): Result<AiResponseModel> {
        return try {
            val response = api.summarizeText(text)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateQuiz(topic: String): Result<AiResponseModel> {
        return try {
            val response = api.generateQuiz(topic)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
