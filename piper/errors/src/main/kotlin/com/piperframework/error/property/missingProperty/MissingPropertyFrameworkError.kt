package com.piperframework.error.property.missingProperty

import com.piperframework.error.FrameworkError
import com.piperframework.error.property.PropertyFrameworkError

/**
 * An error representing that one of the request body properties was missing.
 */
class MissingPropertyFrameworkError(
    override val propetyName: String
) : PropertyFrameworkError() {
    override val key = FrameworkError.Key.MISSING_PROPERTY
    override val message = "No value provided for $propetyName."
}
