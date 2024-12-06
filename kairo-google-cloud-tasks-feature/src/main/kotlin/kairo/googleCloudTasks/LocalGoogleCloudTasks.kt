package kairo.googleCloudTasks

import com.google.inject.Provider
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.rest.client.RestClient
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val logger: KLogger = KotlinLogging.logger {}

public class LocalGoogleCloudTasks(
  private val restClient: Provider<RestClient>,
) : GoogleCloudTasks() {
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  override suspend fun create(endpoint: RestEndpoint<*, *>) {
    logger.info { "Creating task for endpoint: $endpoint." }
    scope.launch {
      restClient.get().request(endpoint)
    }
  }

  override fun close() {
    scope.cancel()
  }
}
