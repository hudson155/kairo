package kairo.rest.exception

public class UnrecognizedProperty(
  override val path: String?,
  override val location: Location?,
) : JsonBadRequestException(
  message = "Unrecognized property." +
    " This property is not recognized. Is it named incorrectly?",
)
