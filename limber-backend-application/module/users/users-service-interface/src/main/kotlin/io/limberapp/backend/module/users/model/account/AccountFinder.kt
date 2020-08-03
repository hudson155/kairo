package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Users
interface AccountFinder {
  fun accountGuid(accountGuid: UUID)
}
