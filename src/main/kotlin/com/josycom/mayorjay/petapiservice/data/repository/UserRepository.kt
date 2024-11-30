package com.josycom.mayorjay.petapiservice.data.repository

import com.josycom.mayorjay.petapiservice.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun existsByEmail(email: String): Boolean
}