package io.limberapp.framework.error

/**
 * This class is the superclass for all JSON errors that are returned. Each key should have a unique
 * meaning.
 */
interface FrameworkError {

    val key: Key
    val message: String

    /**
     * Each Key should have a unique meaning. Keys represent a parameterized version of what went
     * wrong.
     */
    enum class Key {
        INTERNAL,
        MALFORMED_PROPERTY,
        MISSING_PROPERTY,
    }
}
