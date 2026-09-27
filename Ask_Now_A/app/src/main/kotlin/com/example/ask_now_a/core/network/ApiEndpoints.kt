package com.example.ask_now_a.core.network

object ApiEndpoints {

    // Server Base URL (Production Render Cloud Deployment)
    var baseUrl: String = "https://ask-now.onrender.com/asknow/api"

    // AUTH
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val CURRENT_USER = "auth/current-user"

    // SESSIONS
    const val CREATE_SESSION = "session/create"
    const val JOIN_SESSION = "session/join/{id}"
    const val START_SESSION = "session/start/{id}"
    const val END_SESSION = "session/end/{id}"
    const val ALL_LIVE_SESSIONS = "session/all-live"
    const val MY_CREATED_SESSIONS = "session/my-created"
    const val MY_JOINED_SESSIONS = "session/my-joined"

    // REELS / PROBLEM SOLVING & ADS
    const val UPLOAD_REEL = "reels/upload"
    const val REEL_FEED = "reels/feed"
    const val MY_REELS = "reels/my-reels"
    const val LIKE_REEL = "reels/{id}/like"
    const val VIEW_REEL = "reels/{id}/view"
    const val DELETE_REEL = "reels/{id}"
    const val STREAM_MEDIA = "reels/stream/{filename}"
    const val REEL_COMMENTS = "reels/{id}/comments"
    const val DELETE_COMMENT = "reels/comments/{commentId}"

    // ADMIN GOVERNANCE
    const val ADMIN_STATS = "admin/stats"
    const val ADMIN_USERS = "admin/users"
    const val ADMIN_TOGGLE_USER_STATUS = "admin/users/{userId}/toggle-status"
    const val ADMIN_USER_ROLES = "admin/users/{userId}/roles"
    const val ADMIN_DELETE_USER = "admin/users/{userId}"
    const val ADMIN_SESSIONS = "admin/sessions"
    const val ADMIN_FORCE_END_SESSION = "admin/sessions/{sessionId}/end"
    const val ADMIN_DELETE_SESSION = "admin/sessions/{sessionId}"
    const val ADMIN_REELS = "admin/reels"

    // GEMINI AI SMART TUTOR
    const val AI_ASK_DOUBT = "ai/ask-doubt"
    const val AI_SUMMARIZE = "ai/summarize"
    const val AI_GENERATE_QUIZ = "ai/generate-quiz"

    // CHAT & CONNECTIONS
    const val CONNECTIONS = "chat/connections/{userId}"
    const val SEARCH_USERS = "chat/search"
    const val SEND_REQUEST = "chat/request/send"
    const val GET_REQUESTS = "chat/request/list/{userId}"
    const val ACCEPT_REQUEST = "chat/request/accept/{requestId}"
    const val REJECT_REQUEST = "chat/request/reject/{requestId}"
}
