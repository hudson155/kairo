package kairo.gcpSecretSupplier

import kairo.protectedString.ProtectedString

@Suppress("NotImplementedDeclaration")
public object NoopGcpSecretSupplier : GcpSecretSupplier() {
  public override fun get(id: String): ProtectedString? {
    throw NotImplementedError("This GCP secret supplier is no-op.")
  }
}
