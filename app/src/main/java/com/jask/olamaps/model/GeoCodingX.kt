package com.jask.olamaps.model

data class GeoCodingX(
    val error_message: String,
    val info_messages: List<Any>,
    val plus_code: PlusCodeX,
    val results: List<ResultX>,
    val status: String
)