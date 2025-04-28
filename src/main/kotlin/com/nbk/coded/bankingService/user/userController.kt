package com.nbk.coded.bankingService.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/v1")
class UserController(val service: UserService) {

    data class RegisterUserDTO(val username: String, val password: String)

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody dto: RegisterUserDTO): User {
        return service.registerUser(dto.username, dto.password)
    }
    @GetMapping
    fun getAllUsers(): List<User> {
        return service.getAllUsers()
    }
}
