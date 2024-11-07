package kairo.googleCloudScheduler

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoGoogleCloudSchedulerConfig.Noop::class, name = "Noop"),
  JsonSubTypes.Type(KairoGoogleCloudSchedulerConfig.Real::class, name = "Real"),
)
public sealed class KairoGoogleCloudSchedulerConfig {
  public data class JobName(
    val prefix: String?,
    val suffix: String?,
  )

  /**
   * See [NoopJobCreator].
   */
  public data object Noop : KairoGoogleCloudSchedulerConfig()

  /**
   * See [RealJobCreator].
   */
  public data class Real(
    val projectId: String,
    val location: String,
    val jobName: JobName,
    val service: String,
  ) : KairoGoogleCloudSchedulerConfig()
}
