package io.limberapp.module.users.service.user

import com.google.inject.Inject
import io.limberapp.module.users.model.user.UserModel
import io.limberapp.module.users.store.user.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
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
    userStore.delete(userGuid)
  }
}
