package limber.config

public data class GuidsConfig(val generation: Generation) {
  public enum class Generation { Deterministic, Random }
}
