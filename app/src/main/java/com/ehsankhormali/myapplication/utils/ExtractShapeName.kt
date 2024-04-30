package com.ehsankhormali.myapplication.utils

fun extractShapeName(text:String):String{
    val shapeName =
        text.substringAfter(delimiter = "shape name: ", missingDelimiterValue = "")
            .substringBefore(delimiter = "points").trim()
    return shapeName
}