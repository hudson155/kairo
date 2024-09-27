package kairo.rest.exception

public class UnrecognizedPolymorphicType(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException(
  message = "Unrecognized polymorphic type." +
    " This property could be one of several types, but the given type was not recognized.",
)
