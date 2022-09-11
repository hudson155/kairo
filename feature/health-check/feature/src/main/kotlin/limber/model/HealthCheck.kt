package limber.model

import limber.rep.HealthCheckRep

public fun interface HealthCheck {
  public suspend fun check(): HealthCheckRep.State
}
