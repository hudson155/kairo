package kairo.healthCheck

public data class HealthCheckRep(
  val status: Status,
  val checks: Map<String, Check>,
) {
  public enum class Status { Healthy, Unhealthy }

  public data class Check(
    val status: Status,
  )
}
