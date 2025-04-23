package com.nbk.coded.bankingService.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun authenticateUser(@RequestBody authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            authenticationRequest.username,
            authenticationRequest.password
        )

        val authentication: Authentication = authenticationManager.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication

        val username = authentication.name
        val jwt = jwtService.generateToken(username)
        return AuthenticationResponse(jwt)
    }
}

data class AuthenticationRequest(
    val username: String,
    val password: String
)

data class AuthenticationResponse(
    val jwt: String
)
