package com.nbk.coded.bankingService.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken


class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader("Authorization")?.let {
            if (it.startsWith("Bearer ")) it.substring(7) else null
        }

        if (token != null && jwtService.isTokenValid(token, getUsernameFromToken(token))) {
            val username = jwtService.extractUsername(token)
            val userDetails = customUserDetailsService.loadUserByUsername(username)
            val authentication = UsernamePasswordAuthenticationToken(username, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getUsernameFromToken(token: String): String {
        return jwtService.extractUsername(token)
    }
}