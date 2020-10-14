package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.*

interface OrgService {
  fun create(model: OrgModel): OrgModel

  fun get(orgGuid: UUID): OrgModel?

  fun getByOwnerUserGuid(ownerUserGuid: UUID): OrgModel?

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel

  fun delete(orgGuid: UUID)
}
