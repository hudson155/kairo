package kairo.healthCheck

public object HealthCheckRep {
  public data class Liveness(
    val status: Status,
  ) {
    public enum class Status { Healthy, Unhealthy }
  }
}
