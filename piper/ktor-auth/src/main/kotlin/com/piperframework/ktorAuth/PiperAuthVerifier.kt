package com.piperframework.ktorAuth

interface PiperAuthVerifier {

    fun verify(blob: String): PiperAuthPrincipal?
}
