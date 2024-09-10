package kairo.gcpSecretSupplier

import com.google.api.gax.rpc.NotFoundException
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.protectedString.ProtectedString

private val logger: KLogger = KotlinLogging.logger {}

public object DefaultGcpSecretSupplier : GcpSecretSupplier() {
  /**
   * Note: This approach is blocking; it does not leverage Kotlin coroutines.
   */
  public override fun get(id: String): ProtectedString? {
    logger.debug { "Getting GCP secret: $id." }
    return SecretManagerServiceClient.create().use { client ->
      val secretVersionName = SecretVersionName.parse(id)
      val accessResponse = try {
        client.accessSecretVersion(secretVersionName)
      } catch (_: NotFoundException) {
        return@use null
      }
      @OptIn(ProtectedString.Access::class)
      return@use ProtectedString(accessResponse.payload.data.toStringUtf8())
    }
  }
}
