package io.limberapp.service.user

import com.google.inject.Inject
import io.limberapp.api.org.OrgApi
import io.limberapp.client.org.OrgClient
import io.limberapp.exception.user.CannotDeleteOrgOwner
import io.limberapp.model.user.UserModel
import io.limberapp.store.user.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val orgClient: OrgClient,
    private val userStore: UserStore,
) : UserService {
  override fun create(model: UserModel): UserModel =
      userStore.create(model)

  override fun get(userGuid: UUID): UserModel? =
      userStore[userGuid]

  override fun getByEmailAddress(orgGuid: UUID, emailAddress: String): UserModel? =
      userStore.getByEmailAddress(orgGuid, emailAddress)

  override fun getByOrgGuid(orgGuid: UUID): Set<UserModel> =
      userStore.getByOrgGuid(orgGuid)

  override fun update(userGuid: UUID, update: UserModel.Update): UserModel =
      userStore.update(userGuid, update)

  override suspend fun delete(userGuid: UUID) {
    if (orgClient(OrgApi.GetByOwnerUserGuid(userGuid)) != null) throw CannotDeleteOrgOwner()
    userStore.delete(userGuid)
  }
}
