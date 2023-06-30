package com.example.resourceservice.filters

import com.example.resourceservice.exceptions.JWTVerificationException
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.SignedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.net.URL
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.Date

@Component
class LoginFilter: OncePerRequestFilter() {
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
        if (tokenValid(token) && !expiredToken(token)) {
            val user = getUser(token)
            val userDetails = User(user, user, listOf())
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication;
        }
        filterChain.doFilter(request, response)
    }

    private fun expiredToken(token: String): Boolean {
        val expiration: Date = getExpiration(token)
        return expiration.before(Date())
    }

    private fun getUser(token: String): String {
        val signedJWT = SignedJWT.parse(token);
        return signedJWT.jwtClaimsSet.subject
    }

    private fun getExpiration(token: String): Date {
        val signedJWT = SignedJWT.parse(token);
        return signedJWT.jwtClaimsSet.expirationTime
    }
    private fun tokenValid(token: String): Boolean {
        try {
            val signedJWT = SignedJWT.parse(token);
            val jwk = getKey(signedJWT.jwtClaimsSet.jwtid)
            val verifier = RSASSAVerifier(jwk.toRSAKey())
            return signedJWT.verify(verifier)
        } catch (e: NoSuchAlgorithmException) {
            throw JWTVerificationException(e);
        } catch (e: InvalidKeySpecException) {
            throw JWTVerificationException(e);
        }
    }

    private fun getKey(keyId: String): JWK {
        val keysUrl = URL("http://localhost:4000/.well-known/jwks")
        try {
            val jwk = JWKSet.load(keysUrl)
            return jwk.keys.first { it.keyID == keyId }
        } catch (e: Exception) {
            throw JWTVerificationException(e)
        }
    }

}