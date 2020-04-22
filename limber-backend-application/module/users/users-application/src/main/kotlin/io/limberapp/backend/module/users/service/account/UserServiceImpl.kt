package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.exception.account.CannotDeleteUserWithOrgs
import io.limberapp.backend.module.users.store.account.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val userStore: UserStore
) : UserService by userStore {

    override fun delete(userId: UUID) {
        if (orgService.getByOwnerAccountId(userId).isNotEmpty()) throw CannotDeleteUserWithOrgs()
        userStore.delete(userId)
    }
}
