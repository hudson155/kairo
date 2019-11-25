package com.piperframework.error.notFound

import com.piperframework.error.FrameworkError

/**
 * An error representing the HTTP 404 response code.
 */
class NotFoundFrameworkError : FrameworkError {
    override val key = FrameworkError.Key.NOT_FOUND
    override val message = "The entity was not found."
}
