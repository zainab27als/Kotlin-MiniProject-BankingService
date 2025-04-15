package com.nbk.coded.bankingService.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {

    data class RegisterUserDTO(val username: String, val password: String)

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterUserDTO): User {
        return service.registerUser(dto.username, dto.password)
    }
    @GetMapping
    fun getAllUsers(): List<User> {
        return service.getAllUsers()
    }
}
