package io.limberapp.monolith

import io.ktor.application.Application
import io.limberapp.monolith.adhoc.Adhoc
import io.limberapp.monolith.adhoc.adhoc
import io.limberapp.monolith.adhoc.createCovidFeature
import io.limberapp.monolith.adhoc.dbReset
import io.limberapp.monolith.adhoc.onboard
import io.limberapp.monolith.adhoc.updateOwnerUserGuid

private const val LIMBER_TASK = "LIMBER_TASK"

@Suppress("Unused")
internal fun Application.main() {
  when (System.getenv(LIMBER_TASK)) {
    "createCovidFeature" -> adhoc(Adhoc::createCovidFeature)
    "dbReset" -> adhoc(Adhoc::dbReset)
    "onboard" -> adhoc(Adhoc::onboard)
    "updateOwnerUserGuid" -> adhoc(Adhoc::updateOwnerUserGuid)
    null -> LimberMonolith(this)
  }
}
