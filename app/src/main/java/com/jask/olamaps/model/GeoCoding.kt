package com.jask.olamaps.model

data class GeoCoding(
    val error_message: String,
    val info_messages: List<Any>,
    val plus_code: PlusCode,
    val results: List<Result>,
    val status: String
)