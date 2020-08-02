package io.limberapp.backend.module.users.model.account

import java.util.*

interface UserFinder {
  fun orgGuid(orgGuid: UUID)
  fun userGuid(userGuid: UUID)
  fun emailAddress(emailAddress: String)
}
