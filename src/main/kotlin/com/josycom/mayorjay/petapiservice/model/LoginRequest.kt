package com.josycom.mayorjay.petapiservice.model

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,
    @field:NotBlank(message = "Password cannot be blank")
    val password: String
)
