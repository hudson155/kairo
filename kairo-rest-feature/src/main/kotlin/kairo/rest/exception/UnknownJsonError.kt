package kairo.rest.exception

public class UnknownJsonError(cause: Exception) : JsonBadRequestException(
  message = "Unknown JSON error." +
    " Something is wrong with the JSON.",
  cause = cause,
)
