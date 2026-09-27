package com.example.ask_now_a.core.network

import com.example.ask_now_a.features.admin.model.AdminStatsModel
import com.example.ask_now_a.features.admin.model.UserManagementItem
import com.example.ask_now_a.features.ai_tutor.model.AiDoubtRequest
import com.example.ask_now_a.features.ai_tutor.model.AiResponseModel
import com.example.ask_now_a.features.auth.model.AuthResponse
import com.example.ask_now_a.features.auth.model.LoginRequest
import com.example.ask_now_a.features.auth.model.RegisterRequest
import com.example.ask_now_a.features.chat.model.ChatRequestModel
import com.example.ask_now_a.features.chat.model.SendChatRequestPayload
import com.example.ask_now_a.features.chat.model.UserModel
import com.example.ask_now_a.features.reels.model.ReelCommentModel
import com.example.ask_now_a.features.reels.model.ReelModel
import com.example.ask_now_a.features.session.model.CreateSessionRequest
import com.example.ask_now_a.features.session.model.SessionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ================= AUTH =================
    @POST(ApiEndpoints.LOGIN)
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST(ApiEndpoints.REGISTER)
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET(ApiEndpoints.CURRENT_USER)
    suspend fun getCurrentUser(): AuthResponse

    // ================= SESSIONS =================
    @POST(ApiEndpoints.CREATE_SESSION)
    suspend fun createSession(@Body request: CreateSessionRequest): SessionModel

    @POST(ApiEndpoints.JOIN_SESSION)
    suspend fun joinSession(@Path("id") sessionId: Int): Response<Unit>

    @PATCH(ApiEndpoints.START_SESSION)
    suspend fun startSession(@Path("id") sessionId: Int): SessionModel

    @PATCH(ApiEndpoints.END_SESSION)
    suspend fun endSession(@Path("id") sessionId: Int): SessionModel

    @GET(ApiEndpoints.ALL_LIVE_SESSIONS)
    suspend fun getAllLiveSessions(): List<SessionModel>

    @GET(ApiEndpoints.MY_CREATED_SESSIONS)
    suspend fun getMyCreatedSessions(): List<SessionModel>

    @GET(ApiEndpoints.MY_JOINED_SESSIONS)
    suspend fun getMyJoinedSessions(): List<SessionModel>

    // ================= REELS / PROBLEM SOLVING & ADS =================
    @GET(ApiEndpoints.REEL_FEED)
    suspend fun getReelFeed(): List<ReelModel>

    @GET(ApiEndpoints.MY_REELS)
    suspend fun getMyReels(): List<ReelModel>

    @Multipart
    @POST(ApiEndpoints.UPLOAD_REEL)
    suspend fun uploadReel(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("categoryTag") categoryTag: RequestBody?,
        @Part video: MultipartBody.Part,
        @Part thumbnail: MultipartBody.Part?
    ): ReelModel

    @POST(ApiEndpoints.LIKE_REEL)
    suspend fun toggleLikeReel(@Path("id") reelId: Int): ReelModel

    @POST(ApiEndpoints.VIEW_REEL)
    suspend fun incrementReelView(@Path("id") reelId: Int): Response<Unit>

    @DELETE(ApiEndpoints.DELETE_REEL)
    suspend fun deleteReel(@Path("id") reelId: Int): Response<Unit>

    @GET(ApiEndpoints.REEL_COMMENTS)
    suspend fun getReelComments(@Path("id") reelId: Int): List<ReelCommentModel>

    @POST(ApiEndpoints.REEL_COMMENTS)
    suspend fun addReelComment(@Path("id") reelId: Int, @Body body: Map<String, String>): ReelCommentModel

    @DELETE(ApiEndpoints.DELETE_COMMENT)
    suspend fun deleteReelComment(@Path("commentId") commentId: Int): Response<Unit>

    // ================= ADMIN CONTROLS =================
    @GET(ApiEndpoints.ADMIN_STATS)
    suspend fun getAdminStats(): AdminStatsModel

    @GET(ApiEndpoints.ADMIN_USERS)
    suspend fun getAllUsers(): List<UserManagementItem>

    @PATCH(ApiEndpoints.ADMIN_TOGGLE_USER_STATUS)
    suspend fun toggleUserStatus(@Path("userId") userId: Int): Response<Unit>

    @DELETE(ApiEndpoints.ADMIN_DELETE_USER)
    suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>

    @GET(ApiEndpoints.ADMIN_SESSIONS)
    suspend fun getAdminSessions(): List<SessionModel>

    @PATCH(ApiEndpoints.ADMIN_FORCE_END_SESSION)
    suspend fun forceEndSession(@Path("sessionId") sessionId: Int): Response<Unit>

    // ================= GEMINI AI SMART TUTOR =================
    @POST(ApiEndpoints.AI_ASK_DOUBT)
    suspend fun askDoubt(@Body request: AiDoubtRequest): AiResponseModel

    @POST(ApiEndpoints.AI_SUMMARIZE)
    suspend fun summarizeText(@Body text: String): AiResponseModel

    @POST(ApiEndpoints.AI_GENERATE_QUIZ)
    suspend fun generateQuiz(@Query("topic") topic: String): AiResponseModel

    // ================= CHAT & CONNECTIONS =================
    @GET(ApiEndpoints.CONNECTIONS)
    suspend fun getConnections(@Path("userId") userId: Int): List<UserModel>

    @GET(ApiEndpoints.SEARCH_USERS)
    suspend fun searchUsers(@Query("query") query: String): List<UserModel>

    @POST(ApiEndpoints.SEND_REQUEST)
    suspend fun sendChatRequest(@Body payload: SendChatRequestPayload): Response<Unit>

    @GET(ApiEndpoints.GET_REQUESTS)
    suspend fun getChatRequests(@Path("userId") userId: Int): List<ChatRequestModel>

    @POST(ApiEndpoints.ACCEPT_REQUEST)
    suspend fun acceptChatRequest(@Path("requestId") requestId: Int): Response<Unit>

    @POST(ApiEndpoints.REJECT_REQUEST)
    suspend fun rejectChatRequest(@Path("requestId") requestId: Int): Response<Unit>
}
