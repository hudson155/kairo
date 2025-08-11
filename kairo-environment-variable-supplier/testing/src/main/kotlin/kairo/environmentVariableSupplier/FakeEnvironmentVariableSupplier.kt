package kairo.environmentVariableSupplier

public class FakeEnvironmentVariableSupplier(
  map: Map<String, String> = emptyMap(),
) : EnvironmentVariableSupplier() {
  private val map: MutableMap<String, String> = map.toMutableMap()

  override fun get(name: String): String? =
    map[name]

  public operator fun set(name: String, value: String) {
    map[name] = value
  }
}
