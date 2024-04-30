package com.ehsankhormali.myapplication.screens

import androidx.compose.ui.geometry.Offset

data class ShapeScreenUiState (
    val requestState: ShapeScreenRequestState=ShapeScreenRequestState.Initial,
    val shape: String="circle",
    val points:List<Offset> = listOf(Offset(100.0f,100.0f)),
    val radios:List<Int> = listOf(90)

)
