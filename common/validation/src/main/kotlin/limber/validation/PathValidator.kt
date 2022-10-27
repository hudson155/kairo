package limber.validation

public object PathValidator {
  public const val message: String = "must be a valid path"
  public const val pattern: String = "(/$URL_CHAR+)*/?"
  public val regex: Regex = Regex(pattern)
}
