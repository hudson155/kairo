package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.users.model.account.AccountModel
import java.util.*

@LimberModule.Users
interface AccountService {
  fun get(accountGuid: UUID): AccountModel?
}
