package com.piperframework.error.error

import com.piperframework.error.PiperError

/**
 * An error representing the HTTP 409 response code.
 */
class ConflictError : PiperError {
    override val message = "Conflict."
}
