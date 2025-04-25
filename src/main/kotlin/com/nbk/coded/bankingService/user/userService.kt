package com.nbk.coded.bankingService.user

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(username: String, password: String): User {
        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, password = encodedPassword)
        return userRepo.save(user)
    }

    fun getAllUsers(): List<User> {
        return userRepo.findAll()
    }
}


