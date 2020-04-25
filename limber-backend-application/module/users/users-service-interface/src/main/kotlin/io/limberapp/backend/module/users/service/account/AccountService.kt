package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.module.users.model.account.AccountModel
import java.util.UUID

interface AccountService {
    fun get(accountGuid: UUID): AccountModel?
}
