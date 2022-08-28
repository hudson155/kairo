package limber.config

public data class ClockConfig(val type: Type) {
  public enum class Type { Fixed, Real }
}
