package com.piperframework.error.property

import com.piperframework.error.FrameworkError

/**
 * An error representing that something went wrong with one of the request body properties.
 */
abstract class PropertyFrameworkError : FrameworkError {
    abstract val propetyName: String
}
