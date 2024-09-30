package kairo.rest.exception

internal class MalformedJson(
  cause: Exception,
) : JsonBadRequestException(
  message = "Malformed JSON." +
    " The JSON provided was not well-formed.",
  cause = cause,
)
