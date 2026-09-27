package com.example.ask_now_a.features.reels.model

data class ReelModel(
    val id: Int,
    val title: String,
    val description: String? = null,
    val categoryTag: String = "#ProblemSolving",
    val videoUrl: String,
    val thumbnailUrl: String? = null,
    val teacherId: Int = 0,
    val teacherName: String = "Teacher",
    val teacherEmail: String = "",
    var likesCount: Int = 0,
    var commentsCount: Int = 0,
    var viewsCount: Int = 0,
    var likedByCurrentUser: Boolean = false,
    val createdAt: String? = null
)

data class ReelCommentModel(
    val id: Int,
    val text: String,
    val userId: Int,
    val userName: String,
    val userEmail: String? = null,
    val createdAt: String? = null
)
