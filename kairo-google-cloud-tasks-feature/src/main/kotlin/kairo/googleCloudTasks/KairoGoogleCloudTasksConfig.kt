package kairo.googleCloudTasks

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.Http::class, name = "Http"),
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.Local::class, name = "Local"),
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.Noop::class, name = "Noop"),
  JsonSubTypes.Type(KairoGoogleCloudTasksConfig.TransactionAware::class, name = "TransactionAware"),
)
public sealed class KairoGoogleCloudTasksConfig {
  public data class QueueName(
    val prefix: String?,
    val suffix: String?,
  )

  /**
   * See [HttpGoogleCloudTasks].
   */
  public data class Http(
    val baseUrl: String,
    override val projectId: String,
    override val location: String,
    override val queueName: QueueName,
  ) : Real()

  /**
   * See [LocalGoogleCloudTasks].
   */
  public data object Local : KairoGoogleCloudTasksConfig()

  /**
   * See [NoopGoogleCloudTasks].
   */
  public data object Noop : KairoGoogleCloudTasksConfig()

  public sealed class Real : KairoGoogleCloudTasksConfig() {
    public abstract val projectId: String
    public abstract val location: String
    public abstract val queueName: QueueName
  }

  /**
   * See [TransactionAwareGoogleCloudTasks].
   */
  public data class TransactionAware(
    val delegate: KairoGoogleCloudTasksConfig,
  ) : KairoGoogleCloudTasksConfig()
}
