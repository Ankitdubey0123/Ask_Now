package com.example.ask_now_a.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "asknow_prefs")

class LocalStorage(private val context: Context) {

    companion object {
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_TOKEN = stringPreferencesKey("jwt_token")
        private val KEY_ROLE = stringPreferencesKey("user_role")
        private val KEY_NAME = stringPreferencesKey("user_name")
        private val KEY_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun saveUser(id: Int, token: String, role: String, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = id
            prefs[KEY_TOKEN] = token
            prefs[KEY_ROLE] = role
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
        }
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[KEY_TOKEN] }
    val roleFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[KEY_ROLE] }
    val userIdFlow: Flow<Int?> = context.dataStore.data.map { prefs -> prefs[KEY_USER_ID] }
    val userNameFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[KEY_NAME] }
    val userEmailFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[KEY_EMAIL] }

    suspend fun getToken(): String? = context.dataStore.data.map { prefs -> prefs[KEY_TOKEN] }.first()
    suspend fun getUserId(): Int? = context.dataStore.data.map { prefs -> prefs[KEY_USER_ID] }.first()
    suspend fun getRole(): String? = context.dataStore.data.map { prefs -> prefs[KEY_ROLE] }.first()
    suspend fun getName(): String? = context.dataStore.data.map { prefs -> prefs[KEY_NAME] }.first()
    suspend fun getEmail(): String? = context.dataStore.data.map { prefs -> prefs[KEY_EMAIL] }.first()

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
