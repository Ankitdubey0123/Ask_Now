package com.example.ask_now_a.features.reels.repository

import android.content.Context
import com.example.ask_now_a.core.database.AskNowDatabaseHelper
import com.example.ask_now_a.core.network.RetrofitClient
import com.example.ask_now_a.features.reels.model.ReelCommentModel
import com.example.ask_now_a.features.reels.model.ReelModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ReelRepository(context: Context? = null) {

    private val api = RetrofitClient.apiService
    private val dbHelper = context?.let { AskNowDatabaseHelper(it) }

    suspend fun getReelFeed(): Result<List<ReelModel>> {
        return try {
            val networkReels = api.getReelFeed()
            dbHelper?.cacheReels(networkReels)
            Result.success(networkReels)
        } catch (e: Exception) {
            val cached = dbHelper?.getAllCachedReels()
            if (!cached.isNullOrEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getMyReels(): Result<List<ReelModel>> {
        return try {
            val reels = api.getMyReels()
            Result.success(reels)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadReel(title: String, description: String?, categoryTag: String?, videoFile: File): Result<ReelModel> {
        return try {
            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val tagBody = categoryTag?.toRequestBody("text/plain".toMediaTypeOrNull())
            val videoBody = videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
            val videoPart = MultipartBody.Part.createFormData("video", videoFile.name, videoBody)

            val response = api.uploadReel(
                title = titleBody,
                description = descBody,
                categoryTag = tagBody,
                video = videoPart,
                thumbnail = null
            )
            dbHelper?.cacheSingleReel(response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLike(reelId: Int): Result<ReelModel> {
        return try {
            val updated = api.toggleLikeReel(reelId)
            dbHelper?.cacheSingleReel(updated)
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComments(reelId: Int): Result<List<ReelCommentModel>> {
        return try {
            val comments = api.getReelComments(reelId)
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComment(reelId: Int, text: String): Result<ReelCommentModel> {
        return try {
            val comment = api.addReelComment(reelId, mapOf("text" to text))
            Result.success(comment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(commentId: Int): Result<Unit> {
        return try {
            api.deleteReelComment(commentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementView(reelId: Int) {
        try {
            api.incrementReelView(reelId)
        } catch (_: Exception) {}
    }
}
