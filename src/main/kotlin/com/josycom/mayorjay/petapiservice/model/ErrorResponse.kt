package com.josycom.mayorjay.petapiservice.model

data class ErrorResponse(
    val statusCode: Int,
    val message: String,
    val error: List<String>
)