package io.limberapp.backend.module.auth.testing.fixtures.account

import io.limberapp.backend.module.users.rep.account.UserRep
import java.time.LocalDateTime
import java.util.*

internal fun UserRep.Creation.complete(guid: UUID, createdDate: LocalDateTime) = UserRep.Complete(
    guid = guid,
    createdDate = createdDate,
    roles = emptySet(),
    orgGuid = orgGuid,
    firstName = firstName,
    lastName = lastName,
    emailAddress = emailAddress,
    profilePhotoUrl = profilePhotoUrl,
)
