package limber.validation

/**
 * Intentionally doesn't use the normal path validator.
 * Feature paths are stricter (not all paths are allowed).
 */
public object FeaturePathValidator {
  public const val pattern: String = "(/[A-Za-z0-9](-?[A-Za-z0-9])*)+"
  public const val message: String = "must be a valid feature path"
  public val regex: Regex = Regex(pattern)
}
