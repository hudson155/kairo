package kairo.gcpSecretSupplier

import kairo.protectedString.ProtectedString

public class NoopGcpSecretSupplier : GcpSecretSupplier() {
  override suspend fun get(id: String): ProtectedString {
    throw NotImplementedError("This GCP secret supplier is no-op.")
  }
}
