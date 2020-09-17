package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.model.org.OrgFinder
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Orgs
interface OrgService : Finder<OrgModel, OrgFinder> {
  fun create(model: OrgModel): OrgModel

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel

  fun delete(orgGuid: UUID)
}
