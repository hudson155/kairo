package io.limberapp.framework.error.property.validation

import io.limberapp.framework.error.FrameworkError
import io.limberapp.framework.error.property.PropertyFrameworkError

/**
 * An error representing that one of the request body properties did not pass validation.
 */
data class ValidationFrameworkError(
    override val propetyName: String
) : PropertyFrameworkError() {
    override val key = FrameworkError.Key.MALFORMED_PROPERTY
    override val message = "Malformed value provided for $propetyName"
}
