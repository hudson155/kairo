package io.limberapp.framework.error.conflict

import io.limberapp.framework.error.FrameworkError

/**
 * An error representing the HTTP 409 response code.
 */
class ConflictFrameworkError : FrameworkError {
    override val key = FrameworkError.Key.CONFLICT
    override val message = "There was a conflict."
}
