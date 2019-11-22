package io.limberapp.framework.validation

/**
 * The Validation class provides utilities for parameter validation. They're all stored outside of the class in
 * extension functions, or else this class would get huge.
 */
data class Validation<T>(val subject: T, val name: String)
