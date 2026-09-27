package com.example.ask_now_a.features.ai_tutor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_now_a.features.ai_tutor.repository.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AiUiState(
    val isLoading: Boolean = false,
    val aiResponse: String? = null,
    val error: String? = null
)

class AiViewModel : ViewModel() {

    private val repository = AiRepository()

    private val _uiState = MutableStateFlow(AiUiState())
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    fun askDoubt(question: String?, base64Image: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, aiResponse = null)
            val result = repository.askDoubt(question, base64Image, "image/jpeg")
            result.onSuccess { res ->
                _uiState.value = _uiState.value.copy(isLoading = false, aiResponse = res.result)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Gemini AI failed: ${e.message}")
            }
        }
    }

    fun summarizeText(text: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, aiResponse = null)
            val result = repository.summarizeText(text)
            result.onSuccess { res ->
                _uiState.value = _uiState.value.copy(isLoading = false, aiResponse = res.result)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Summarization failed: ${e.message}")
            }
        }
    }

    fun generateQuiz(topic: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, aiResponse = null)
            val result = repository.generateQuiz(topic)
            result.onSuccess { res ->
                _uiState.value = _uiState.value.copy(isLoading = false, aiResponse = res.result)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Quiz generation failed: ${e.message}")
            }
        }
    }
}
