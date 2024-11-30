package com.josycom.mayorjay.petapiservice.service

import com.josycom.mayorjay.petapiservice.data.entity.User
import com.josycom.mayorjay.petapiservice.data.mapper.toUser
import com.josycom.mayorjay.petapiservice.data.repository.UserRepository
import com.josycom.mayorjay.petapiservice.model.LoginRequest
import com.josycom.mayorjay.petapiservice.model.RegisterRequest
import com.josycom.mayorjay.petapiservice.model.UserResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger: Logger = LoggerFactory.getLogger("UserService")

    fun registerUser(request: RegisterRequest): UserResponse {
        try {
            val userExists = userRepository.existsByEmail(request.email)
            return when {
                userExists -> {
                    logger.info("Email provided already exists: ${request.email}")
                    UserResponse(description = "Email provided is already in use.")
                }
                else -> {
                    val user = request.toUser(passwordEncoder.encode(request.password))
                    val savedUser = userRepository.save(user)
                    logger.info("User registered successfully: ${request.email}")
                    UserResponse(
                        code = 0,
                        description = "Success",
                        firstName = savedUser.firstName,
                        lastName = savedUser.lastName,
                        userId = savedUser.id
                    )
                }
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while registering user: $ex")
            return UserResponse(description = ex.toString())
        }
    }

    fun loginUser(request: LoginRequest): UserResponse {
        try {
            val existingUserOptional = findByEmail(request.email)
            if (existingUserOptional.isEmpty) {
                logger.info("Incorrect email provided: ${request.email}")
                return UserResponse(description = "Email or password is incorrect.")
            }

            val existingUser = existingUserOptional.get()
            return when {
                !passwordEncoder.matches(request.password, existingUser.password) -> {
                    logger.info("Incorrect password provided: ${request.email}")
                    UserResponse(description = "Email or password is incorrect")
                }

                else -> {
                    logger.info("User logged in successfully: ${request.email}")
                    UserResponse(
                        code = 0,
                        description = "Success",
                        firstName = existingUser.firstName,
                        lastName = existingUser.lastName,
                        userId = existingUser.id
                    )
                }
            }

        } catch (ex: Exception) {
            logger.error("Error occurred while logging in user: $ex")
            return UserResponse(description = ex.toString())
        }
    }

    fun findByEmail(email: String): Optional<User> = userRepository.findByEmail(email)
}