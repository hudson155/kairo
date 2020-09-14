package io.limberapp.common.ktorAuth

import io.ktor.auth.Principal

/**
 * Represents one way of verifying a [Principal].
 */
interface LimberAuthVerifier<P : Principal> {
  fun verify(blob: String): P?
}
