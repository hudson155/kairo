package com.piperframework.error.error

import com.piperframework.error.FrameworkError

/**
 * An error representing that one of the request body properties did not pass validation.
 */
class ValidationError(
    propetyName: String
) : FrameworkError {
    override val key = FrameworkError.Key.MALFORMED_PROPERTY
    override val message = "Malformed value provided for $propetyName."
}
