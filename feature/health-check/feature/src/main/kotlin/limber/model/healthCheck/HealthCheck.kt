package limber.model.healthCheck

import limber.rep.healthCheck.HealthCheckRep

public fun interface HealthCheck {
  public suspend fun check(): HealthCheckRep.State
}
