package com.josycom.mayorjay.petapiservice.util

import com.josycom.mayorjay.petapiservice.data.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> = emptyList()

    override fun getPassword(): String = user.email

    override fun getUsername(): String = user.password
}