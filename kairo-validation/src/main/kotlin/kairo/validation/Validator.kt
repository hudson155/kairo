package kairo.validation

@Suppress("MaximumLineLength")
public object Validator {
  /**
   * https://html.spec.whatwg.org/multipage/input.html#valid-e-mail-address
   */
  public val emailAddress: Regex =
    Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:[.][a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
}
