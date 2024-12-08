package com.josycom.mayorjay.petapiservice.data.repository

import com.josycom.mayorjay.petapiservice.data.entity.AccessToken
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenRepository : JpaRepository<AccessToken, Long> {

    @Transactional
    fun deleteAccessTokenByToken(token: String)
}