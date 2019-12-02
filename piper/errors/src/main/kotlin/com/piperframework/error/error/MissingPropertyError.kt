package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing that one of the request body properties was missing.
 */
class MissingPropertyError(
    propetyName: String
) : FrameworkError {
    override val key = FrameworkError.Key.MISSING_PROPERTY
    override val message = "No value provided for $propetyName."
}
