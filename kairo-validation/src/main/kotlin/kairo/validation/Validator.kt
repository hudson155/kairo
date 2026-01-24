package kairo.validation

/**
 * Provides common validation patterns.
 */
public object Validator {
  /**
   * Email address regex from the WHATWG HTML Standard.
   * https://html.spec.whatwg.org/multipage/input.html#valid-e-mail-address
   */
  public val emailAddress: Regex =
    Regex(
      """^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""",
    )
}
