package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing the HTTP 400 response code.
 */
class BadRequestError : FrameworkError {
    override val message = "Bad Request."
}
