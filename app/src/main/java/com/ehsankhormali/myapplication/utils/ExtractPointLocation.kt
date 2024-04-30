package com.ehsankhormali.myapplication.utils

import androidx.compose.ui.geometry.Offset

fun extractPointLocations(text: String):List<Offset> {

    var pointLocations: List<Offset> = emptyList()
    val regex = Regex("""\((-?\d+(?:\.\d*)?),(-?\d+(?:\.\d*)?)\)""")
    val matches = regex.findAll(text)

    pointLocations = matches.map { matchResult ->
        val groups = matchResult.groups
        val x = groups[1]?.value?.toFloatOrNull() ?: 0f
        val y = groups[2]?.value?.toFloatOrNull() ?: 0f
        Offset(x, y)
    }.toList()
    return pointLocations
}