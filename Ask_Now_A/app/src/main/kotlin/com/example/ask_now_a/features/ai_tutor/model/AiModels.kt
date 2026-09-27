package com.example.ask_now_a.features.ai_tutor.model

data class AiDoubtRequest(
    val question: String? = null,
    val base64Image: String? = null,
    val mimeType: String? = "image/jpeg"
)

data class AiResponseModel(
    val result: String = "",
    val action: String = ""
)
