package com.piperframework.authentication

import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.JWTVerifier
import com.auth0.jwt.interfaces.Payload
import java.util.Base64

interface TokenVerifier {
    fun verify(blob: String): Payload
}

class JwtVerifier(private val jwtVerifier: JWTVerifier) : TokenVerifier {
    override fun verify(blob: String): Payload {
        val jwt = jwtVerifier.verify(blob)
        val payloadString = String(Base64.getUrlDecoder().decode(jwt.payload))
        return JWTParser().parsePayload(payloadString)
    }
}
