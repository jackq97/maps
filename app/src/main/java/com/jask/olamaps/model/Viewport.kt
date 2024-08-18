package com.jask.olamaps.model

import com.jask.olamaps.model.geocoding.Southwest

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)