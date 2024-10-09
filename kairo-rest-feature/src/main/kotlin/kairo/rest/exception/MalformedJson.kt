package kairo.rest.exception

public class MalformedJson(
  cause: Exception,
) : JsonBadRequestException(
  message = "Malformed JSON." +
    " The JSON provided was not well-formed.",
  cause = cause,
)
