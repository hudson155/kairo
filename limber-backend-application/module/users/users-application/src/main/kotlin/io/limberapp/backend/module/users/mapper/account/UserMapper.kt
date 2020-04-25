package io.limberapp.backend.module.users.mapper.account

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.rep.account.UserRep
import java.time.Clock
import java.time.LocalDateTime

internal class UserMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {
    fun model(rep: UserRep.Creation) = UserModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        identityProvider = false,
        superuser = false,
        orgGuid = rep.orgGuid,
        firstName = rep.firstName,
        lastName = rep.lastName,
        emailAddress = rep.emailAddress,
        profilePhotoUrl = rep.profilePhotoUrl
    )

    fun completeRep(model: UserModel) = UserRep.Complete(
        guid = model.guid,
        createdDate = model.createdDate,
        roles = JwtRole.values().filter { model.hasRole(it) },
        orgGuid = model.orgGuid,
        firstName = model.firstName,
        lastName = model.lastName,
        emailAddress = model.emailAddress,
        profilePhotoUrl = model.profilePhotoUrl
    )

    fun update(rep: UserRep.Update) = UserModel.Update(
        // Roles (identityProvider and superuser) are updated by role endpoints, not by the user update rep.
        identityProvider = null,
        superuser = null,

        firstName = rep.firstName,
        lastName = rep.lastName
    )
}
