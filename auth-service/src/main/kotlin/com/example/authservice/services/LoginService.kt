package com.example.authservice.services

import com.example.authservice.dtos.LoginDto
import com.example.authservice.dtos.TokenDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date


@Service
class LoginService(
    @Value("\${jwt.secret}")
    private val secret: String,
) {
    companion object {
        private const val JWT_TOKEN_VALIDITY: Int = 1 * 60 * 60
    }
    fun login(loginDto: LoginDto): TokenDto {
        val claims: Map<String, Any> = HashMap()

        val token = Jwts.builder()
            .setClaims(claims)
            .setSubject(loginDto.user)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()

         return TokenDto(token)
    }

    private fun getSignInKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}