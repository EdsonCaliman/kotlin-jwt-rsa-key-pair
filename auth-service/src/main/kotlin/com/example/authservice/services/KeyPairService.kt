package com.example.authservice.services

import com.example.authservice.exceptions.ServiceException
import com.example.authservice.models.KeyPairContainer
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.UUID


@Service
class KeyPairService {
    private var keyPairContainer: KeyPairContainer = generateKeyPair()

    fun getKeyPair(): KeyPair {
        return keyPairContainer.keyPair
    }

    fun getJwk(): JWKSet {
        return keyPairContainer.jwkKey
    }

    fun getKeyId(): String {
        return keyPairContainer.keyId
    }

    private fun generateKeyPair(): KeyPairContainer {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")

            keyPairGenerator.initialize(2048, SecureRandom.getInstanceStrong());

            val keyPair = keyPairGenerator.generateKeyPair()
            val keyId = UUID.randomUUID().toString()

            val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey)
                .privateKey(keyPair.private as RSAPrivateKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(keyId)
                .build();

            return KeyPairContainer(keyId, keyPair, JWKSet(rsaKey.toPublicJWK()))

        } catch (e: NoSuchAlgorithmException) {
            throw ServiceException(e)
        }
    }

}