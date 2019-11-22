package io.limberapp.framework.ktorAuth

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Credential
import io.ktor.auth.Principal

class LimberAuthCredential(val payload: Payload) : Credential

class JWTPrincipal(val payload: Payload) : Principal

internal class JWTAuthSchemes(val defaultScheme: String, vararg additionalSchemes: String) {
    private val schemes = (arrayOf(defaultScheme) + additionalSchemes).toSet()
    private val schemesLowerCase = schemes.map { it.toLowerCase() }.toSet()
    operator fun contains(scheme: String): Boolean = scheme.toLowerCase() in schemesLowerCase
}
