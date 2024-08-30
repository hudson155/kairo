package kairo.healthCheck

public abstract class HealthCheckService {
  internal fun liveness(): HealthCheckRep.Liveness =
    HealthCheckRep.Liveness(
      status = HealthCheckRep.Liveness.Status.Healthy,
    )
}
