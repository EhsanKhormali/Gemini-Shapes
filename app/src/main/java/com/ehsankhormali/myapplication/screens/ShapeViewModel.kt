package com.ehsankhormali.myapplication.screens

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehsankhormali.myapplication.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShapeViewModel:ViewModel() {
    private val _uiState: MutableStateFlow<ShapeUiState> =
        MutableStateFlow(ShapeUiState.Initial)
    val uiState: StateFlow<ShapeUiState> =
        _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = ShapeUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = ShapeUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = ShapeUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}