package io.limberapp.framework.error

/**
 * This class is the superclass for all JSON errors that are returned.
 */
interface FrameworkError {

    val key: Key
    val message: String

    /**
     * Keys represent a parameterized version of what went wrong; their generic meaning should be
     * unique.
     */
    enum class Key {
        CONFLICT,
        FORBIDDEN,
        INTERNAL,
        MALFORMED_PROPERTY,
        MISSING_PROPERTY,
        NOT_FOUND,
    }
}
