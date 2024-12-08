package com.josycom.mayorjay.petapiservice.controller

import com.josycom.mayorjay.petapiservice.model.LoginRequest
import com.josycom.mayorjay.petapiservice.model.RegisterRequest
import com.josycom.mayorjay.petapiservice.model.UserResponse
import com.josycom.mayorjay.petapiservice.service.UserService
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> =
        ResponseEntity.ok(userService.registerUser(request))

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<UserResponse> =
        ResponseEntity.ok(userService.loginUser(request))

    @PostMapping("logout", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun logout(@RequestBody formData: MultiValueMap<String, String>): ResponseEntity<Void> {
        userService.logout(formData["token"]?.get(0).orEmpty())
        return ResponseEntity.noContent().build()
    }
}