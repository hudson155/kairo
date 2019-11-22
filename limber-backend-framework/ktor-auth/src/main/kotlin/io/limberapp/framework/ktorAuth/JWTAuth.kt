package io.limberapp.framework.ktorAuth

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.Authentication
import io.ktor.auth.AuthenticationContext
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationFunction
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Credential
import io.ktor.auth.Principal
import io.ktor.auth.UnauthorizedResponse
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.request.ApplicationRequest
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import java.util.Base64

private val JWTAuthKey: Any = "JWTAuth"

class JWTCredential(val payload: Payload) : Credential

class JWTPrincipal(val payload: Payload) : Principal

class JWTAuthenticationProvider internal constructor(config: Configuration) :
    AuthenticationProvider(config) {

    internal val realm: String = config.realm
    internal val schemes: JWTAuthSchemes = config.schemes
    internal val authHeader: (ApplicationCall) -> HttpAuthHeader? = config.authHeader
    internal val verifier: ((HttpAuthHeader) -> JWTVerifier?) = config.verifier
    internal val authenticationFunction = config.authenticationFunction
    internal val challengeFunction: JWTAuthChallengeFunction = config.challenge

    class Configuration internal constructor(name: String?) :
        AuthenticationProvider.Configuration(name) {

        internal var authenticationFunction: AuthenticationFunction<JWTCredential> = { null }

        internal var schemes = JWTAuthSchemes("Bearer")

        internal var authHeader: (ApplicationCall) -> HttpAuthHeader? =
            { call -> call.request.parseAuthorizationHeaderOrNull() }

        internal var verifier: ((HttpAuthHeader) -> JWTVerifier?) = { null }

        internal var challenge: JWTAuthChallengeFunction = { scheme, realm ->
            call.respond(
                UnauthorizedResponse(
                    HttpAuthHeader.Parameterized(
                        scheme,
                        mapOf(HttpAuthHeader.Parameters.Realm to realm)
                    )
                )
            )
        }

        var realm: String = "Ktor Server"

        fun verifier(verifier: (HttpAuthHeader) -> JWTVerifier?) {
            this.verifier = verifier
        }

        fun validate(validate: suspend ApplicationCall.(JWTCredential) -> Principal?) {
            authenticationFunction = validate
        }

        internal fun build() = JWTAuthenticationProvider(this)
    }
}

internal class JWTAuthSchemes(val defaultScheme: String, vararg additionalSchemes: String) {
    val schemes = (arrayOf(defaultScheme) + additionalSchemes).toSet()
    val schemesLowerCase = schemes.map { it.toLowerCase() }.toSet()
    operator fun contains(scheme: String): Boolean = scheme.toLowerCase() in schemesLowerCase
}

fun Authentication.Configuration.jwt(
    name: String? = null,
    configure: JWTAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = JWTAuthenticationProvider.Configuration(name).apply(configure).build()
    val realm = provider.realm
    val authenticate = provider.authenticationFunction
    val verifier = provider.verifier
    val schemes = provider.schemes
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val token = provider.authHeader(call)
        if (token == null) {
            context.bearerChallenge(
                AuthenticationFailedCause.NoCredentials,
                realm,
                schemes,
                provider.challengeFunction
            )
            return@intercept
        }

        try {
            val principal = verifyAndValidate(
                call,
                verifier(token),
                token,
                schemes,
                authenticate
            )
            if (principal != null) {
                context.principal(principal)
            } else {
                context.bearerChallenge(
                    AuthenticationFailedCause.InvalidCredentials,
                    realm,
                    schemes,
                    provider.challengeFunction
                )
            }
        } catch (cause: Throwable) {
            val message = cause.message ?: cause.javaClass.simpleName
            context.error(JWTAuthKey, AuthenticationFailedCause.Error(message))
        }
    }
    register(provider)
}

typealias JWTAuthChallengeFunction =
        suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String, realm: String) -> Unit

private fun AuthenticationContext.bearerChallenge(
    cause: AuthenticationFailedCause,
    realm: String,
    schemes: JWTAuthSchemes,
    challengeFunction: JWTAuthChallengeFunction
) = challenge(JWTAuthKey, cause) {
    challengeFunction(this, schemes.defaultScheme, realm)
    if (!it.completed && call.response.status() != null) {
        it.complete()
    }
}

private suspend fun verifyAndValidate(
    call: ApplicationCall,
    jwtVerifier: JWTVerifier?,
    token: HttpAuthHeader,
    schemes: JWTAuthSchemes,
    validate: suspend ApplicationCall.(JWTCredential) -> Principal?
): Principal? {
    val jwt = try {
        token.getBlob(schemes)?.let { jwtVerifier?.verify(it) }
    } catch (ex: JWTVerificationException) {
        null
    } ?: return null

    val payload = jwt.parsePayload()
    val credentials = JWTCredential(payload)
    return validate(call, credentials)
}

private fun HttpAuthHeader.getBlob(schemes: JWTAuthSchemes) = when {
    this is HttpAuthHeader.Single && authScheme in schemes -> blob
    else -> null
}

private fun ApplicationRequest.parseAuthorizationHeaderOrNull() = try {
    parseAuthorizationHeader()
} catch (ex: IllegalArgumentException) {
    null
}

private fun DecodedJWT.parsePayload(): Payload {
    val payloadString = String(Base64.getUrlDecoder().decode(payload))
    return JWTParser().parsePayload(payloadString)
}
