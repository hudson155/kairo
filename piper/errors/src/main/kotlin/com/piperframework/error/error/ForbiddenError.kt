package com.piperframework.error.error

import com.piperframework.error.PiperError

/**
 * An error representing the HTTP 403 response code.
 */
class ForbiddenError : PiperError {
    override val message = "Forbidden."
}
