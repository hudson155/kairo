package io.limberapp.framework.validation.validation

/**
 * ValidationError should be thrown when a property fails validation. It should be handled in the
 * application's exception mapper.
 */
class ValidationError(val propertyName: String) : Exception()
