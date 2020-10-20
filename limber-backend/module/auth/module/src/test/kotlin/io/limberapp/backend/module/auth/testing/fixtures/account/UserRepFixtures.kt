package io.limberapp.backend.module.auth.testing.fixtures.account

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.util.string.joinNames
import java.time.ZonedDateTime
import java.util.*

internal fun UserRep.Creation.complete(guid: UUID, createdDate: ZonedDateTime) = UserRep.Complete(
    guid = guid,
    createdDate = createdDate,
    roles = emptySet(),
    orgGuid = orgGuid,
    firstName = firstName,
    lastName = lastName,
    fullName = listOfNotNull(firstName, lastName).joinNames(),
    emailAddress = emailAddress,
    profilePhotoUrl = profilePhotoUrl,
)
