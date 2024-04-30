package com.ehsankhormali.myapplication.screens

sealed interface ShapeScreenRequestState {
    /**
     * A sealed hierarchy describing the state of the text generation.
     */

        /**
         * Empty state when the screen is first shown
         */
        object Initial : ShapeScreenRequestState

        /**
         * Still loading
         */
        object Loading : ShapeScreenRequestState

        /**
         * Text has been generated
         */
        data class Success(val outputText: String) : ShapeScreenRequestState

        /**
         * There was an error generating text
         */
        data class Error(val errorMessage: String) : ShapeScreenRequestState
}