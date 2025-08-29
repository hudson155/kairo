package kairo.gcpSecretSupplier

import kairo.protectedString.ProtectedString

/**
 * Lightweight and coroutine-friendly Google Secret Manager wrapper for Kotlin.
 * Use [DefaultGcpSecretSupplier] in production.
 */
public abstract class GcpSecretSupplier {
  public abstract suspend operator fun get(id: String): ProtectedString?
}
