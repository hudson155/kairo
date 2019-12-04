package com.piperframework.error

/**
 * This class is the superclass for all JSON errors that are returned.
 */
interface PiperError {
    val statusCode: Int
    val message: String
}
