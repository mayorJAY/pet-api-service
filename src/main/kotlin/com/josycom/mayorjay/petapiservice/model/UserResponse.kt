package com.josycom.mayorjay.petapiservice.model

data class UserResponse(
    val code: Int = -1,
    val description: String = "Error processing request",
    val firstName: String? = null,
    val lastName: String? = null,
    val userId: Long? = null
)
