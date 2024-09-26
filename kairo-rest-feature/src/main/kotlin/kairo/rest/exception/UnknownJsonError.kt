package kairo.rest.exception

public class UnknownJsonError : JsonBadRequestException(
  message = "Unknown JSON error." +
    " Something is wrong with the JSON.",
) {
  public companion object {
    internal fun create(): UnknownJsonError =
      UnknownJsonError()
  }
}
