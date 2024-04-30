package com.ehsankhormali.myapplication.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehsankhormali.myapplication.BuildConfig
import com.ehsankhormali.myapplication.utils.extractPointLocations
import com.ehsankhormali.myapplication.utils.extractShapeName
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShapeViewModel:ViewModel() {
    private val _uiState: MutableStateFlow<ShapeScreenUiState> =
        MutableStateFlow(ShapeScreenUiState())
    val uiState: StateFlow<ShapeScreenUiState> =
        _uiState.asStateFlow()
    var userPrompt by mutableStateOf("")
        private set


    fun updateUserPrompt(prompt: String) {
        userPrompt = prompt
    }
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = ShapeScreenUiState(requestState = ShapeScreenRequestState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    // Handle the generated content
                    val pints= extractPointLocations(outputContent)
                    val shapeName= extractShapeName(outputContent)
                    _uiState.value=ShapeScreenUiState(points = pints,requestState =ShapeScreenRequestState.Success(outputContent), shape = shapeName)

                }
            } catch (e: Exception) {
                _uiState.value = ShapeScreenUiState(requestState =ShapeScreenRequestState.Error(e.localizedMessage ?: ""))
            }
        }
    }
}