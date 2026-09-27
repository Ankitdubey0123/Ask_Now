package com.example.ask_now_a.features.chat.model

data class UserModel(
    val id: Int,
    val name: String,
    val email: String
)

data class ChatRequestModel(
    val id: Int,
    val senderId: Int,
    val senderName: String
)

data class SendChatRequestPayload(
    val senderId: Int,
    val receiverId: Int
)

data class ChatMessage(
    val id: String = "",
    val sender: String,
    val receiver: String,
    val message: String,
    val time: String = ""
)
