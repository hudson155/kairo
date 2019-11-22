package io.limberapp.framework.ktorAuth

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.AuthenticationContext
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.UnauthorizedResponse
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.request.ApplicationRequest
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

typealias JWTAuthChallengeFunction =
        suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String, realm: String) -> Unit

class LimberAuthProvider internal constructor(
    private val config: LimberAuthConfig
) : AuthenticationProvider(config) {

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

    private fun PipelineContext<AuthenticationContext, ApplicationCall>.intercept(
        context: AuthenticationContext
    ) {

        val token = call.request.parseAuthorizationHeaderOrNull()
            ?: return context.bearerChallenge(AuthenticationFailedCause.NoCredentials)

        try {
            val principal = getPrincipal(token)
                ?: return context.bearerChallenge(AuthenticationFailedCause.InvalidCredentials)
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

    private fun getPrincipal(token: HttpAuthHeader): JWTPrincipal? {
        if (token !is HttpAuthHeader.Single) return null
        val verifier = config.verifiers[token.authScheme] ?: return null
        val credential = verifier.verify(token.blob) ?: return null
        return JWTPrincipal(credential.payload)
    }

    private fun HttpAuthHeader.getBlob(schemes: JWTAuthSchemes) = when {
        this is HttpAuthHeader.Single && authScheme in schemes -> blob
        else -> null
    }

    private fun AuthenticationContext.bearerChallenge(
        cause: AuthenticationFailedCause
    ) = challenge(config.authKey, cause) {
        challengeFunction(this, config.defaultScheme, config.realm)
        if (!it.completed && call.response.status() != null) it.complete()
    }

    private fun AuthenticationContext.handleError(e: Throwable) {
        val message = e.message ?: e.javaClass.simpleName
        error(config.authKey, AuthenticationFailedCause.Error(message))
    }
}
