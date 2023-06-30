package com.example.authservice.models

import com.nimbusds.jose.jwk.JWKSet
import java.security.KeyPair

class KeyPairContainer(
    val keyId: String,
    val keyPair: KeyPair,
    val jwkKey: JWKSet
)