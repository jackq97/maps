package com.jask.olamaps.model

data class ResultX(
    val address_components: List<AddressComponentX>,
    val formatted_address: String,
    val geometry: GeometryX,
    val layer: List<String>,
    val name: String,
    val place_id: String,
    val plus_code: PlusCodeX,
    val types: List<Any>
)