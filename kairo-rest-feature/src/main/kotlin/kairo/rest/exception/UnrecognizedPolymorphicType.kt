package kairo.rest.exception

import kairo.rest.exceptionHandler.JacksonHandler

/**
 * Thrown from [JacksonHandler]
 * Not intended to be thrown externally.
 */
public class UnrecognizedPolymorphicType(
  override val path: String?,
  override val location: Location?,
  cause: Exception,
) : JsonBadRequestException(
  message = "Unrecognized polymorphic type." +
    " This property could be one of several types, but the given type was not recognized.",
  cause = cause,
)
