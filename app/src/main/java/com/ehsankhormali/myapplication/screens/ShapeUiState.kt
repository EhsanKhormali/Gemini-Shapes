package com.ehsankhormali.myapplication.screens

sealed interface ShapeUiState {
    /**
     * A sealed hierarchy describing the state of the text generation.
     */

        /**
         * Empty state when the screen is first shown
         */
        object Initial : ShapeUiState

        /**
         * Still loading
         */
        object Loading : ShapeUiState

        /**
         * Text has been generated
         */
        data class Success(val outputText: String) : ShapeUiState

        /**
         * There was an error generating text
         */
        data class Error(val errorMessage: String) : ShapeUiState
}