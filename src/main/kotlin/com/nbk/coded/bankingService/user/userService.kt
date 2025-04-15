package com.nbk.coded.bankingService.user

import org.springframework.stereotype.Service

@Service
class UserService(val userRepo: UserRepository) {
    fun registerUser(username: String, password: String): User {
        val user = User(username = username, password = password)
        return userRepo.save(user)
    }
    fun getAllUsers(): List<User> {
        return userRepo.findAll()
    }
}
