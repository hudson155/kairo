package io.limberapp.auth

import io.ktor.auth.Principal
import io.limberapp.auth.jwt.JwtPrincipal
import io.limberapp.restInterface.EndpointHandler

/**
 * Implementations must verify the given authorization header value. If the header value can be
 * mapped to a valid JWT, a [JwtPrincipal] should be returned. If it's not, an exception should be
 * thrown.
 *
 * [EndpointHandler] currently only works with [JwtPrincipal]s. If there's a use case in the future
 * to generify this class to work with any [Principal] rather than just [JwtPrincipal], make sure
 * [EndpointHandler] is updated accordingly.
 */
internal interface AuthVerifier {
  fun verify(authorizationHeader: String): JwtPrincipal
}
