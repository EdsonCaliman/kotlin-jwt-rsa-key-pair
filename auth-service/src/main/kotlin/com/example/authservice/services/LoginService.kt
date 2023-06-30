package com.example.authservice.services

import com.example.authservice.dtos.LoginDto
import com.example.authservice.dtos.TokenDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.Date


@Service
class LoginService(
    private val keyPairService: KeyPairService
) {
    companion object {
        private const val JWT_TOKEN_VALIDITY: Int = 1 * 60 * 60
    }
    fun login(loginDto: LoginDto): TokenDto {
        val claims: Map<String, Any> = HashMap()

        val keyPair = keyPairService.getKeyPair()

        val token = Jwts.builder()
            .setClaims(claims)
            .setId(keyPairService.getKeyId())
            .setSubject(loginDto.user)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .compact()

         return TokenDto(token)
    }
}