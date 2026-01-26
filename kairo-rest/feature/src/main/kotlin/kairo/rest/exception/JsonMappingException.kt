package kairo.rest.exception

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException
import com.fasterxml.jackson.databind.exc.InvalidNullException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import kairo.exception.LogicalFailure

internal fun JsonMappingException.toLogicalFailure(): LogicalFailure? =
  when (this) {
    is UnrecognizedPropertyException -> UnrecognizedProperty(this)
    is IgnoredPropertyException -> UnrecognizedProperty(this)
    is InvalidTypeIdException -> UnrecognizedTypeDiscriminator(this)
    is InvalidNullException -> MissingProperty(this)
    is MismatchedInputException -> InvalidProperty(this)
    else -> null
  }

internal fun jacksonPathReference(path: List<JsonMappingException.Reference>): String =
  path.mapNotNull { reference ->
    reference.fieldName?.let { return@mapNotNull it }
    reference.index.takeIf { it >= 0 }?.let { return@mapNotNull it }
    return@mapNotNull null
  }.joinToString(separator = "/", prefix = "/")
