package kairo.rest.exception

internal class UnknownJsonError(cause: Exception) : JsonBadRequestException(
  message = "Unknown JSON error." +
    " Something is wrong with the JSON.",
  cause = cause,
)
