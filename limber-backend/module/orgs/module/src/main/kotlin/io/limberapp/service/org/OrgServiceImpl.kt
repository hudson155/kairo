package io.limberapp.service.org

import com.google.inject.Inject
import io.limberapp.model.org.OrgModel
import io.limberapp.store.org.OrgStore
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
) : OrgService {
  override fun create(model: OrgModel): OrgModel =
      orgStore.create(model)

  override fun get(orgGuid: UUID): OrgModel? =
      orgStore[orgGuid]

  override fun getByOwnerUserGuid(ownerUserGuid: UUID): OrgModel? =
      orgStore.getByOwnerUserGuid(ownerUserGuid)

  override fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel =
      orgStore.update(orgGuid, update)

  override fun delete(orgGuid: UUID): Unit =
      orgStore.delete(orgGuid)
}
