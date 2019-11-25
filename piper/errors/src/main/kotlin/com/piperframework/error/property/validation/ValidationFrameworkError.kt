package com.piperframework.error.property.validation

import com.piperframework.error.FrameworkError
import com.piperframework.error.property.PropertyFrameworkError

/**
 * An error representing that one of the request body properties did not pass validation.
 */
class ValidationFrameworkError(
    override val propetyName: String
) : PropertyFrameworkError() {
    override val key = FrameworkError.Key.MALFORMED_PROPERTY
    override val message = "Malformed value provided for $propetyName."
}
