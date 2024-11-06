package kairo.googleCloudTasks

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.Noop::class, name = "Noop"),
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.Real::class, name = "Real"),
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.TransactionAware::class, name = "TransactionAware"),
)
public sealed class KairoGoogleCloudTasksConfig {
  public data class QueueName(
    val prefix: String?,
    val suffix: String?,
  )

  /**
   * See [NoopTaskCreator].
   */
  public data object Noop : KairoGoogleCloudTasksConfig()

  /**
   * See [RealTaskCreator].
   */
  public data class Real(
    val projectId: String,
    val location: String,
    val queueName: QueueName,
    val service: String,
  ) : KairoGoogleCloudTasksConfig()

  /**
   * See [TransactionAwareTaskCreator].
   */
  public data class TransactionAware(
    val delegate: KairoGoogleCloudTasksConfig,
  ) : KairoGoogleCloudTasksConfig()
}
