package limber.validation

public object HostnameValidator {
  public const val message: String = "must be a valid hostname"
  public const val pattern: String = "$HOSTNAME_PORTION?(\\.$HOSTNAME_PORTION?)*"
  public val regex: Regex = Regex(pattern)
}
