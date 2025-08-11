package kairo.gcpSecretSupplier

import com.google.api.core.ApiFutureToListenableFuture
import com.google.api.gax.rpc.NotFoundException
import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.guava.await

private val logger: KLogger = KotlinLogging.logger {}

public class DefaultGcpSecretSupplier : GcpSecretSupplier() {
  private val client: SecretManagerServiceClient = SecretManagerServiceClient.create()

  /**
   * Note: This approach is blocking; it does not leverage Kotlin coroutines.
   */
  override suspend fun get(id: String): ProtectedString? {
    logger.debug { "Getting GCP secret: $id." }
    val accessRequest = AccessSecretVersionRequest.newBuilder().apply {
      name = id
    }.build()
    val accessResponse = try {
      client.accessSecretVersionCallable()
        .futureCall(accessRequest)
        .let { ApiFutureToListenableFuture(it) }
        .await()
    } catch (_: NotFoundException) {
      return null
    }
    @OptIn(ProtectedString.Access::class)
    return ProtectedString(accessResponse.payload.data.toStringUtf8())
  }
}
