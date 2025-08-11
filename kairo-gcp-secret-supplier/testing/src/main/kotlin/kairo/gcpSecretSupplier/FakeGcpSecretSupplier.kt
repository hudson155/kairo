package kairo.gcpSecretSupplier

import kairo.protectedString.ProtectedString

public class FakeGcpSecretSupplier(
  map: Map<String, ProtectedString> = emptyMap(),
) : GcpSecretSupplier() {
  private val map: MutableMap<String, ProtectedString> = map.toMutableMap()

  override suspend fun get(id: String): ProtectedString? =
    map[id]

  public operator fun set(id: String, value: ProtectedString) {
    map[id] = value
  }
}
