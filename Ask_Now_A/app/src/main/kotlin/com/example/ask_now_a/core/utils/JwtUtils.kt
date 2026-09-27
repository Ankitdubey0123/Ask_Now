package com.example.ask_now_a.core.utils

import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets

object JwtUtils {

    fun getRoleFromToken(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size >= 2) {
                val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(payloadJson)
                jsonObject.optString("role", "STUDENT")
            } else {
                "STUDENT"
            }
        } catch (e: Exception) {
            "STUDENT"
        }
    }
}
