package com.example.ask_now_a.features.admin.model

data class AdminStatsModel(
    val totalUsers: Int = 0,
    val totalStudents: Int = 0,
    val totalTeachers: Int = 0,
    val totalAdmins: Int = 0,
    val totalSessions: Int = 0,
    val totalLiveSessions: Int = 0,
    val totalCompletedSessions: Int = 0,
    val totalReels: Int = 0,
    val totalReelViews: Int = 0
)

data class UserManagementItem(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val enabled: Boolean = true
)
