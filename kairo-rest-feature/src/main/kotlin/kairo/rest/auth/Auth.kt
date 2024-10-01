package kairo.rest.auth

import kairo.exception.KairoException

@Suppress("ConvertObjectToDataObject", "UseDataClass")
public class Auth(
  public val principal: Principal?,
) {
  public sealed class Result {
    public object Success : Result()

    public class Exception(public val e: KairoException) : Result()
  }
}
