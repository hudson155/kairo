package kairo.rest.exception

public class MalformedJson : JacksonBadRequestException(
  message = "Malformed JSON." +
    " The JSON provided was not well-formed.",
) {
  public companion object {
    internal fun create(): MalformedJson =
      MalformedJson()
  }
}
