package com.jask.olamaps.model

import com.jask.olamaps.model.geocoding.Southwest

data class ViewportX(
    val northeast: NortheastX,
    val southwest: Southwest
)