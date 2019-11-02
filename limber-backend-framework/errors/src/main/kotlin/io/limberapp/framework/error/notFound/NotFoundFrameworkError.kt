package io.limberapp.framework.error.notFound

import io.limberapp.framework.error.FrameworkError

/**
 * An error representing that the entity in question was not found
 */
class NotFoundFrameworkError : FrameworkError {
    override val key = FrameworkError.Key.NOT_FOUND
    override val message = "The entity was not found"
}
