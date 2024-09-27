package kairo.rest.exception

public class MalformedJson : JsonBadRequestException(
  message = "Malformed JSON." +
    " The JSON provided was not well-formed.",
)
