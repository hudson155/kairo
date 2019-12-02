package com.piperframework.error.error

import com.piperframework.error.PiperError

/**
 * An error representing the HTTP 404 response code.
 */
class NotFoundError : PiperError {
    override val message = "The entity was not found."
}
