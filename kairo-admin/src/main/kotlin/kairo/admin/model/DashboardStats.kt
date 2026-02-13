package kairo.admin.model

@Suppress("LongParameterList")
public data class DashboardStats(
  val endpointCount: Int,
  val configFileCount: Int,
  val tableCount: Int,
  val jvmUptime: String,
  val featureCount: Int,
  val healthCheckCount: Int,
  val integrationCount: Int,
  val dependencyCount: Int,
  val errorCount: Int,
  val slackChannelCount: Int? = null,
  val stytchModuleCount: Int? = null,
  val emailTemplateCount: Int? = null,
)
