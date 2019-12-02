package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing the HTTP 409 response code.
 */
class ConflictError : FrameworkError {
    override val message = "Conflict."
}
