package limber.validation

public object Regex {
  public object Hostname {
    public const val regex: String = "$hostnamePortion?(\\.$hostnamePortion?)*"
    public const val message: String = "must be a valid hostname"
  }

  private const val hostnamePortion: String = "[A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9]"
}
