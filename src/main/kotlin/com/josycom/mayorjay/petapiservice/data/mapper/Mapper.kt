package com.josycom.mayorjay.petapiservice.data.mapper

import com.josycom.mayorjay.petapiservice.data.entity.User
import com.josycom.mayorjay.petapiservice.model.RegisterRequest

fun RegisterRequest.toUser(password: String): User {
    return User(
        firstName = this.firstName,
        lastName = this.lastName,
        age = this.age,
        gender = this.gender,
        email = this.email,
        password = password
    )
}