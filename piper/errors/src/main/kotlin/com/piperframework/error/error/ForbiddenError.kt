package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing the HTTP 403 response code.
 */
class ForbiddenError : FrameworkError {
    override val message = "Forbidden."
}
