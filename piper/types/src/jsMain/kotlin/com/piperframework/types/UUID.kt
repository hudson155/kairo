package com.piperframework.types

/**
 * In JS, UUIDs use the [String] class instead of an actual UUID class. UUID validation is still performed by the
 * conversion service.
 */
actual typealias UUID = String
