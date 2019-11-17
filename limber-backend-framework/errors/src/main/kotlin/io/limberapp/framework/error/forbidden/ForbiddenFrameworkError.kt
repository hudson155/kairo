package io.limberapp.framework.error.forbidden

import io.limberapp.framework.error.FrameworkError

/**
 * An error representing that access was forbidden.
 */
class ForbiddenFrameworkError : FrameworkError {
    override val key = FrameworkError.Key.FORBIDDEN
    override val message = "Access forbidden."
}
