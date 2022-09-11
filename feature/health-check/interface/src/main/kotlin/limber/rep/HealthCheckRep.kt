package limber.rep

public data class HealthCheckRep(
  val state: State,
  val checks: Map<String, Check>,
) {
  public enum class State { Healthy, Unhealthy }

  public data class Check(
    val name: String,
    val state: State,
  )
}
