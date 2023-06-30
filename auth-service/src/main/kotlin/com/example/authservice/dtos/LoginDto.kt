package com.example.authservice.dtos

import jakarta.validation.constraints.NotBlank

class LoginDto {
    @NotBlank
    lateinit var user: String
    @NotBlank
    lateinit var password: String
}