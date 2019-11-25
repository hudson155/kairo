package com.piperframework.ktorAuth

interface LimberAuthVerifier {

    fun verify(blob: String): LimberAuthPrincipal?
}
