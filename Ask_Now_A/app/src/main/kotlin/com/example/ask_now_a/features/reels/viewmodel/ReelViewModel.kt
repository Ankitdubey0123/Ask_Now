package com.example.ask_now_a.features.reels.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.features.reels.model.ReelCommentModel
import com.example.ask_now_a.features.reels.model.ReelModel
import com.example.ask_now_a.features.reels.repository.ReelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class ReelUiState(
    val isLoading: Boolean = false,
    val reels: List<ReelModel> = emptyList(),
    val error: String? = null,
    val uploadSuccess: Boolean = false,
    val activeComments: List<ReelCommentModel> = emptyList(),
    val isCommentsLoading: Boolean = false,
    val activeReelIdForComments: Int? = null
)

class ReelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReelRepository(application)

    private val _uiState = MutableStateFlow(ReelUiState())
    val uiState: StateFlow<ReelUiState> = _uiState.asStateFlow()

    init {
        fetchFeed()
    }

    fun fetchFeed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getReelFeed()
            result.onSuccess { reels ->
                _uiState.value = _uiState.value.copy(isLoading = false, reels = reels)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load reels feed: ${e.message}")
            }
        }
    }

    fun uploadReel(title: String, description: String?, categoryTag: String?, videoFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, uploadSuccess = false)
            val result = repository.uploadReel(title, description, categoryTag, videoFile)
            result.onSuccess { newReel ->
                val updatedList = listOf(newReel) + _uiState.value.reels
                _uiState.value = _uiState.value.copy(isLoading = false, reels = updatedList, uploadSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Upload failed: ${e.message}")
            }
        }
    }

    fun toggleLike(reelId: Int) {
        viewModelScope.launch {
            val result = repository.toggleLike(reelId)
            result.onSuccess { updatedReel ->
                val currentList = _uiState.value.reels.toMutableList()
                val index = currentList.indexOfFirst { it.id == reelId }
                if (index != -1) {
                    currentList[index] = updatedReel
                    _uiState.value = _uiState.value.copy(reels = currentList)
                }
            }
        }
    }

    fun fetchComments(reelId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCommentsLoading = true, activeReelIdForComments = reelId)
            val result = repository.getComments(reelId)
            result.onSuccess { comments ->
                _uiState.value = _uiState.value.copy(isCommentsLoading = false, activeComments = comments)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isCommentsLoading = false)
            }
        }
    }

    fun postComment(reelId: Int, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val result = repository.addComment(reelId, text)
            result.onSuccess { newComment ->
                val updatedComments = listOf(newComment) + _uiState.value.activeComments
                val currentList = _uiState.value.reels.toMutableList()
                val index = currentList.indexOfFirst { it.id == reelId }
                if (index != -1) {
                    val currentReel = currentList[index]
                    currentList[index] = currentReel.copy(commentsCount = currentReel.commentsCount + 1)
                }
                _uiState.value = _uiState.value.copy(activeComments = updatedComments, reels = currentList)
            }
        }
    }

    fun deleteComment(reelId: Int, commentId: Int) {
        viewModelScope.launch {
            val result = repository.deleteComment(commentId)
            result.onSuccess {
                val updatedComments = _uiState.value.activeComments.filter { it.id != commentId }
                val currentList = _uiState.value.reels.toMutableList()
                val index = currentList.indexOfFirst { it.id == reelId }
                if (index != -1) {
                    val currentReel = currentList[index]
                    val newCount = maxOf(0, currentReel.commentsCount - 1)
                    currentList[index] = currentReel.copy(commentsCount = newCount)
                }
                _uiState.value = _uiState.value.copy(activeComments = updatedComments, reels = currentList)
            }
        }
    }

    fun registerView(reelId: Int) {
        viewModelScope.launch {
            repository.incrementView(reelId)
        }
    }
}
