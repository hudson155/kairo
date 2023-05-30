package limber.config

public data class IdsConfig(val generation: Generation) {
  public enum class Generation { Deterministic, Random }
}
