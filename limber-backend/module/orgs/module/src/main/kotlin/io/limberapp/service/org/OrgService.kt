package io.limberapp.service.org

import io.limberapp.model.org.OrgModel
import java.util.UUID

interface OrgService {
  fun create(model: OrgModel): OrgModel

  operator fun get(orgGuid: UUID): OrgModel?

  fun getByOwnerUserGuid(ownerUserGuid: UUID): OrgModel?

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel

  fun delete(orgGuid: UUID)
}
