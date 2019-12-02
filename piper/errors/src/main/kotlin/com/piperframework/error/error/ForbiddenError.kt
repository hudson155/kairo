package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing that access was forbidden.
 */
class ForbiddenError : FrameworkError {
    override val key = FrameworkError.Key.FORBIDDEN
    override val message = "Access forbidden."
}
