package com.josycom.mayorjay.petapiservice.security

import com.josycom.mayorjay.petapiservice.data.repository.UserRepository
import com.josycom.mayorjay.petapiservice.util.UserDetails
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class BeanProvider(
    private val userRepository: UserRepository
) {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            val user = userRepository.findByEmail(username)
            if (user.isEmpty) {
                throw UsernameNotFoundException("User not found")
            }
            UserDetails(user.orElseThrow { UsernameNotFoundException("User not found") })
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}