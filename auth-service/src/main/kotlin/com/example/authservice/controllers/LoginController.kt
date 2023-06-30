package com.example.authservice.controllers

import com.example.authservice.dtos.LoginDto
import com.example.authservice.dtos.TokenDto
import com.example.authservice.services.KeyPairService
import com.example.authservice.services.LoginService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val loginService: LoginService,
    private val keyPairService: KeyPairService
) {
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): TokenDto {
        return loginService.login(loginDto)
    }

    @GetMapping("/.well-known/jwks")
    fun getJwk(): MutableMap<String, Any>? {
        return keyPairService.getJwk().toJSONObject(true)
    }
}