package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Users
interface UserFinder {
  fun orgGuid(orgGuid: UUID)
  fun userGuid(userGuid: UUID)
  fun emailAddress(emailAddress: String)
}
