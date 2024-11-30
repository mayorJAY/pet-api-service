package com.josycom.mayorjay.petapiservice.controller

import com.josycom.mayorjay.petapiservice.model.LoginRequest
import com.josycom.mayorjay.petapiservice.model.RegisterRequest
import com.josycom.mayorjay.petapiservice.model.UserResponse
import com.josycom.mayorjay.petapiservice.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> =
        ResponseEntity(userService.registerUser(request), HttpStatus.OK)

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<UserResponse> =
        ResponseEntity(userService.loginUser(request), HttpStatus.OK)
}