package limber.validation

/**
 * Validates icon names from Google Fonts' Material Icons: https://fonts.google.com/icons.
 * Does this roughly, rather than comparing to a master list.
 */
public object IconNameValidator {
  public const val message: String = "must be a valid icon name"
  public const val pattern: String = "[a-z0-9_]{2,63}"
  public val regex: Regex = Regex(pattern)
}
