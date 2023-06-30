package com.example.resourceservice.filters

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.Key
import java.util.Date

@Component
class LoginFilter(
    @Value("\${jwt.secret}")
    private val secret: String,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.split(" ")[1].trim()
        if (validateToken(token)) {
            val user = getUser(token)
            val userDetails = User(user, user, listOf())
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication;
        }
        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String): Boolean {
        val expiration: Date = getExpiration(token)

        if (expiration.before(Date())) {
            throw Exception("Token expired")
        }

        return true
    }

    private fun getUser(token: String): String {
        val jwtToken = Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
        val claims = jwtToken.parseClaimsJws(token).body
        return claims.subject
    }

    private fun getExpiration(token: String): Date {
        val jwtToken = Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
        val claims = jwtToken.parseClaimsJws(token).body
        return claims.expiration
    }

    private fun getSignInKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}