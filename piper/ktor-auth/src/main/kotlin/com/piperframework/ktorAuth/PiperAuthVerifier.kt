package com.piperframework.ktorAuth

import io.ktor.auth.Principal

/**
 * Represents one way of verifying a [Principal].
 */
interface PiperAuthVerifier<P : Principal> {

    fun verify(blob: String): P?
}
