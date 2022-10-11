package limber.validation

public object Regex {
  public object Hostname {
    public const val regex: String = "$hostnamePortion?(\\.$hostnamePortion?)*"
    public const val message: String = "must be a valid hostname"
  }

  public object Path {
    public const val regex: String = "(/$urlChar+)*/?"
    public const val message: String = "must be a valid path"
  }

  private const val hostnamePortion: String = "[A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9]"
  private const val urlChar: String = "[A-Za-z0-9\\-_.%?=#&]"
}
