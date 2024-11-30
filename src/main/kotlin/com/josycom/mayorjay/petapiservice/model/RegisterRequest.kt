package com.josycom.mayorjay.petapiservice.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank(message = "First name cannot be blank")
    val firstName: String,
    @field:NotBlank(message = "Last name cannot be blank")
    val lastName: String,
    @field:Min(value = 1,  message = "Age cannot be less than 1")
    val age: Int,
    @field:NotBlank(message = "Gender cannot be blank")
    val gender: String,
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,
    @field:NotBlank(message = "Password cannot be blank")
    val password: String
)
