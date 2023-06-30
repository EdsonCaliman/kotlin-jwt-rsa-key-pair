# kotlin-jwt-rsa-key-pair

## In this project have the two approaches to work with jwt

### at the commit: Using secret key in auth-service and resource-service

This way uses JWT with a secret key, but is necessary to define in auth-service and in the resource-service, it's more commom approache

### at the commit: Using JWT security with JWKS key in auth-service and resource-service

This way uses JWT with a pair of keys, a private and a public key, in the auth-service the keys are defined, the private is protected in the server, but for the acess the public key have a endpoint ".well-known/jwks" to return the key in the JWK format, so the resource-service can consume the endpoint and validate the jwt token

### References
https://www.unosquare.com/blog/why-and-how-to-improve-jwt-security-with-jwks-key-rotation-in-java/

https://ruleoftech.com/2020/generating-jwt-and-jwk-for-information-exchange-between-services

https://youtu.be/KQPuPbaf7vk

https://github.com/ali-bouali/spring-boot-3-jwt-security/tree/main

https://connect2id.com/products/nimbus-jose-jwt/examples