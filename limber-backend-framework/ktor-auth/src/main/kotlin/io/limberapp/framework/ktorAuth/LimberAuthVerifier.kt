package io.limberapp.framework.ktorAuth

interface LimberAuthVerifier {

    fun verify(blob: String): LimberAuthCredential?
}
