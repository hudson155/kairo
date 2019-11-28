package com.piperframework.ktorAuth

import io.ktor.auth.Principal

interface PiperAuthVerifier<P : Principal> {

    fun verify(blob: String): P?
}
