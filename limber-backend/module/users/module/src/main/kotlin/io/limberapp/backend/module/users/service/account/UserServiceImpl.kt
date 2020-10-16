package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.store.account.UserStore
import java.util.*

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore,
    private val orgClient: OrgClient,
) : UserService {
  override fun create(model: UserModel) =
      userStore.create(model)

  override fun get(userGuid: UUID) =
      userStore.get(userGuid)

  override fun getByEmailAddress(orgGuid: UUID, emailAddress: String) =
      userStore.getByEmailAddress(orgGuid, emailAddress)

  override fun getByOrgGuid(orgGuid: UUID) =
      userStore.getByOrgGuid(orgGuid)

  override fun update(userGuid: UUID, update: UserModel.Update) =
      userStore.update(userGuid, update)

  override suspend fun delete(userGuid: UUID) {
    if (orgClient(OrgApi.GetByOwnerUserGuid(userGuid)) != null) throw CannotDeleteOrgOwner()
    userStore.delete(userGuid)
  }
}
