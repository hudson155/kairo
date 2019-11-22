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

typealias JWTAuthChallengeFunction =
        suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String, realm: String) -> Unit

class JWTAuthenticationProvider internal constructor(
    private val verifier: (HttpAuthHeader) -> JWTVerifier?
) : AuthenticationProvider(object : Configuration(name = null) {}) {

    // TODO: Extract these and verifier into a config and make them mutable by the caller.
    private val authKey = "LimberAuth"
    private val realm = "Limber Server"
    private val schemes = JWTAuthSchemes("Bearer")

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
        pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { intercept(it) }
    }

    private suspend fun PipelineContext<AuthenticationContext, ApplicationCall>.intercept(
        context: AuthenticationContext
    ) {

        val token = call.request.parseAuthorizationHeaderOrNull()
            ?: return context.bearerChallenge(AuthenticationFailedCause.NoCredentials)

        try {
            val principal = verifyAndValidate(
                call = call,
                jwtVerifier = verifier(token),
                token = token,
                schemes = schemes,
                validate = this@JWTAuthenticationProvider.authenticationFunction
            ) ?: return context.bearerChallenge(AuthenticationFailedCause.InvalidCredentials)
            context.principal(principal)
        } catch (e: Throwable) {
            context.handleError(e)
        }
    }

    private fun ApplicationRequest.parseAuthorizationHeaderOrNull() = try {
        parseAuthorizationHeader()
    } catch (e: IllegalArgumentException) {
        null
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

    private fun DecodedJWT.parsePayload(): Payload {
        val payloadString = String(Base64.getUrlDecoder().decode(payload))
        return JWTParser().parsePayload(payloadString)
    }

    private fun AuthenticationContext.bearerChallenge(
        cause: AuthenticationFailedCause
    ) = challenge(authKey, cause) {
        challengeFunction(this, schemes.defaultScheme, realm)
        if (!it.completed && call.response.status() != null) it.complete()
    }

    private fun AuthenticationContext.handleError(e: Throwable) {
        val message = e.message ?: e.javaClass.simpleName
        error(authKey, AuthenticationFailedCause.Error(message))
    }
}
