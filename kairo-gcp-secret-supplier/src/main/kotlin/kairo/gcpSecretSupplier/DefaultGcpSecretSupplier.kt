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

  override suspend fun get(id: String): ProtectedString? {
    logger.debug { "Getting GCP secret (id=$id)." }
    val request = AccessSecretVersionRequest.newBuilder().apply {
      this.name = id
    }.build()
    val response = try {
      client.accessSecretVersionCallable()
        .futureCall(request)
        .let { ApiFutureToListenableFuture(it) }
        .await()
    } catch (_: NotFoundException) {
      return null // TODO: QA that this 404 works.
    }
    @OptIn(ProtectedString.Access::class)
    return response.payload.data.toStringUtf8().let { ProtectedString(it) }
  }
}
