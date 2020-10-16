package io.limberapp.backend.module.users.model.account

import java.util.*

interface AccountFinder {
  fun accountGuid(accountGuid: UUID)
}
