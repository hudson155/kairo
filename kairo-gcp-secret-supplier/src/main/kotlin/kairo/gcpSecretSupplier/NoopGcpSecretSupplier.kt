package kairo.gcpSecretSupplier

public class NoopGcpSecretSupplier : GcpSecretSupplier() {
  override suspend fun get(id: String): Nothing {
    throw NotImplementedError("This GCP secret supplier is no-op.")
  }
}
