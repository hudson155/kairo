package kairo.gcpSecretSupplier

import kairo.protectedString.ProtectedString

/**
 * Supplies GCP secrets.
 * The default implementation ([DefaultGcpSecretSupplier]) delegates to the GCP Secret Manager SDK.
 * This abstract class is for testability.
 */
public abstract class GcpSecretSupplier {
  public abstract fun get(id: String): ProtectedString?
}
