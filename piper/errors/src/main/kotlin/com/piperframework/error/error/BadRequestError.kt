package com.piperframework.error.error

import com.piperframework.error.PiperError

/**
 * An error representing the HTTP 400 response code.
 */
class BadRequestError : PiperError {
    override val statusCode = 400
    override val message = "Bad Request."
}
