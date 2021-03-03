package io.limberapp.rep.user

import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.util.string.fullName
import java.util.UUID

internal fun UserRep.Creation.complete(guid: UUID): UserRep.Complete =
    UserRep.Complete(
        guid = guid,
        permissions = LimberPermissions.none(),
        orgGuid = orgGuid,
        emailAddress = emailAddress,
        firstName = firstName,
        lastName = lastName,
        fullName = fullName(firstName, lastName),
        profilePhotoUrl = profilePhotoUrl,
    )
