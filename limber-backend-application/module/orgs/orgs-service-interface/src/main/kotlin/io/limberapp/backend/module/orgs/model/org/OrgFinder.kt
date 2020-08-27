package io.limberapp.backend.module.orgs.model.org

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Orgs
interface OrgFinder {
  fun orgGuid(orgGuid: UUID)
  fun ownerUserGuid(ownerUserGuid: UUID)
}
