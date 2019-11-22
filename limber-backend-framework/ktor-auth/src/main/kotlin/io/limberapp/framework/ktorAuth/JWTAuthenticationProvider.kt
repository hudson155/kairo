package io.limberapp.framework.ktorAuth

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.AuthenticationContext
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationFunction
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Principal
import io.ktor.auth.UnauthorizedResponse
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.request.ApplicationRequest
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import java.util.Base64

private const val JWT_AUTH_KEY = "JWTAuth"

typealias JWTAuthChallengeFunction =
        suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String, realm: String) -> Unit

class JWTAuthenticationProvider internal constructor(verifier: (HttpAuthHeader) -> JWTVerifier?) :
    AuthenticationProvider(object : Configuration(name = null) {}) {

    private val authHeader: (ApplicationCall) -> HttpAuthHeader? =
        { call -> call.request.parseAuthorizationHeaderOrNull() }
    private val authenticationFunction: AuthenticationFunction<JWTCredential> =
        { credential -> JWTPrincipal(credential.payload) }
    private val challengeFunction: JWTAuthChallengeFunction = { scheme, realm ->
        call.respond(
            UnauthorizedResponse(
                HttpAuthHeader.Parameterized(
                    scheme,
                    mapOf(HttpAuthHeader.Parameters.Realm to realm)
                )
            )
        )
    }

    init {

        val realm = "Ktor Server"
        val schemes = JWTAuthSchemes("Bearer")
        pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
            val token = authHeader(call)
            if (token == null) {
                context.bearerChallenge(
                    cause = AuthenticationFailedCause.NoCredentials,
                    realm = realm,
                    schemes = schemes,
                    challengeFunction = challengeFunction
                )
                return@intercept
            }

            try {
                val principal = verifyAndValidate(
                    call = call,
                    jwtVerifier = verifier(token),
                    token = token,
                    schemes = schemes,
                    validate = this@JWTAuthenticationProvider.authenticationFunction
                )
                if (principal != null) {
                    context.principal(principal)
                } else {
                    context.bearerChallenge(
                        cause = AuthenticationFailedCause.InvalidCredentials,
                        realm = realm,
                        schemes = schemes,
                        challengeFunction = challengeFunction
                    )
                }
            } catch (cause: Throwable) {
                val message = cause.message ?: cause.javaClass.simpleName
                context.error(
                    JWT_AUTH_KEY,
                    AuthenticationFailedCause.Error(message)
                )
            }
        }
    }
}

private fun AuthenticationContext.bearerChallenge(
    cause: AuthenticationFailedCause,
    realm: String,
    schemes: JWTAuthSchemes,
    challengeFunction: JWTAuthChallengeFunction
) = challenge(JWT_AUTH_KEY, cause) {
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
