package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.exception.account.CannotDeleteUserWithOrgs
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.store.account.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val userStore: UserStore
) : UserService {
    override fun create(model: UserModel) = userStore.create(model)

    override fun get(userGuid: UUID) = userStore.get(userGuid)

    override fun getByEmailAddress(emailAddress: String) = userStore.getByEmailAddress(emailAddress)

    override fun update(userGuid: UUID, update: UserModel.Update) = userStore.update(userGuid, update)

    override fun delete(userGuid: UUID) {
        if (orgService.getByOwnerAccountGuid(userGuid).isNotEmpty()) throw CannotDeleteUserWithOrgs()
        userStore.delete(userGuid)
    }
}
