package com.jask.olamaps.model.geocoding

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)