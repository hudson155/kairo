package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.module.users.model.account.AccountModel
import java.util.*

interface AccountService {
  fun get(accountGuid: UUID): AccountModel?
}
