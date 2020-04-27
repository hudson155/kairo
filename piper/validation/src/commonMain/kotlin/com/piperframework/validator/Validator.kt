package com.piperframework.validator

private const val DARB_CHUNK_SIZE = 4 // Warning, changing this alone will break the code.

/**
 * This object contains methods to validate primitive inputs.
 */
@Suppress("MagicNumber")
object Validator {
    fun auth0ClientId(value: String) = Regex.auth0ClientId.matches(value)

    fun base64EncodedUuid(value: String) = Regex.base64EncodedUuid.matches(value)

    fun darb(value: String): Boolean {
        // Use Regex, but the Regex doesn't entirely validate the DARB.
        if (!Regex.darb.matches(value)) return false

        // DARB always has 2 components separated by a dot, and no dots elsewhere in the syntax.
        val components = value.split('.')
        if (components.size != 2) return false

        // The first component is the size (positive).
        val size = components[0].toInt()
        if (size < 0) return false

        // The second component is the hex, the length of which must correlate with the size.
        val hex = components[1]
        // This math works due to integer rounding.
        if (hex.length != (size + DARB_CHUNK_SIZE - 1) / DARB_CHUNK_SIZE) {
            return false
        }

        return true
    }

    fun emailAddress(value: String) = Regex.emailAddress.matches(value)

    fun featureName(value: String) = value.length in 3..20

    fun hostname(value: String) = Regex.hostname.matches(value)

    fun humanName(value: String) = value.length in 1..40

    fun length1hundred(value: String, allowEmpty: Boolean) = value.length in (if (allowEmpty) 0 else 1)..100

    fun length10thousand(value: String, allowEmpty: Boolean) = value.length in (if (allowEmpty) 0 else 1)..10_000

    fun orgName(value: String) = value.length in 3..40

    fun orgRoleName(value: String) = value.length in 3..40

    fun path(value: String) = Regex.path.matches(value)

    /**
     * This URL validator is definitely not perfect, but there really is no such thing as a perfect URL validator - just
     * one that fits the use case.
     */
    fun url(value: String) = Regex.url.matches(value)

    fun uuid(value: String) = Regex.uuid.matches(value)
}
