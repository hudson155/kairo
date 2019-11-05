package io.limberapp.framework.error.property.missingProperty

import io.limberapp.framework.error.FrameworkError
import io.limberapp.framework.error.property.PropertyFrameworkError

/**
 * An error representing that one of the request body properties was missing.
 */
class MissingPropertyFrameworkError(
    override val propetyName: String
) : PropertyFrameworkError() {
    override val key = FrameworkError.Key.MISSING_PROPERTY
    override val message = "No value provided for $propetyName."
}
