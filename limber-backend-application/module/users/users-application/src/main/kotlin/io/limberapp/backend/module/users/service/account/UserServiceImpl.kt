package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.model.account.UserFinder
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.store.account.UserStore
import java.util.*

internal class UserServiceImpl @Inject constructor(
  @OptIn(LimberModule.Orgs::class) private val orgService: OrgService,
  private val userStore: UserStore,
) : UserService, Finder<UserModel, UserFinder> by userStore {
  override fun create(model: UserModel) =
    userStore.create(model)

  override fun update(userGuid: UUID, update: UserModel.Update) =
    userStore.update(userGuid, update)

  @LimberModule.Orgs
  override fun delete(userGuid: UUID) {
    if (orgService.findOnlyOrNull { ownerAccountGuid(userGuid) } != null) throw CannotDeleteOrgOwner()
    userStore.delete(userGuid)
  }
}
