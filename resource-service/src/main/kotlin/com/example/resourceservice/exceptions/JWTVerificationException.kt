package com.example.resourceservice.exceptions

class JWTVerificationException(exception: Exception): RuntimeException(exception) {
}