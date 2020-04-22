package com.piperframework.util

/**
 * Encodes a URL component.
 */
@Suppress("UnusedPrivateMember") // False positive - Detekt bug?
expect fun enc(value: Any): String
