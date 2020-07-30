package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.*

@LimberModule.Orgs
interface OrgService {
  fun create(model: OrgModel): OrgModel

  fun get(orgGuid: UUID): OrgModel?

  fun getByOwnerAccountGuid(ownerAccountGuid: UUID): OrgModel?

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel

  fun delete(orgGuid: UUID)
}
