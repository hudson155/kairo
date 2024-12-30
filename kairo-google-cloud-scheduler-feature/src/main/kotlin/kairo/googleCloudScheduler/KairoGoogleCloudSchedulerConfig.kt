package kairo.googleCloudScheduler

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoGoogleCloudSchedulerConfig.GoogleAppEngine::class, name = "GoogleAppEngine"),
  JsonSubTypes.Type(KairoGoogleCloudSchedulerConfig.Http::class, name = "Http"),
  JsonSubTypes.Type(KairoGoogleCloudSchedulerConfig.Noop::class, name = "Noop"),
)
public sealed class KairoGoogleCloudSchedulerConfig {
  public data class JobName(
    val prefix: String?,
    val suffix: String?,
  )

  /**
   * See [GoogleAppEngineGoogleCloudScheduler].
   */
  public data class GoogleAppEngine(
    override val projectId: String,
    override val location: String,
    override val jobName: JobName,
    val service: String,
  ) : Real()

  /**
   * See [HttpGoogleCloudScheduler].
   */
  public data class Http(
    val baseUrl: String,
    override val projectId: String,
    override val location: String,
    override val jobName: JobName,
  ) : Real()

  /**
   * See [NoopGoogleCloudScheduler].
   */
  public data object Noop : KairoGoogleCloudSchedulerConfig()

  public sealed class Real : KairoGoogleCloudSchedulerConfig() {
    public abstract val projectId: String
    public abstract val location: String
    public abstract val jobName: JobName
  }
}
