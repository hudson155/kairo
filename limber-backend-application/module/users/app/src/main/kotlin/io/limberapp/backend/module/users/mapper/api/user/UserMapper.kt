package io.limberapp.backend.module.users.mapper.api.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.rep.user.UserRep
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class UserMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun model(rep: UserRep.Creation) = UserModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        firstName = rep.firstName,
        lastName = rep.lastName,
        emailAddress = rep.emailAddress,
        profilePhotoUrl = rep.profilePhotoUrl,
        roles = emptySet()
    )

    fun completeRep(model: UserModel) = UserRep.Complete(
        id = model.id,
        created = model.created,
        firstName = model.firstName,
        lastName = model.lastName,
        emailAddress = model.emailAddress,
        profilePhotoUrl = model.profilePhotoUrl,
        roles = model.roles
    )

    fun update(rep: UserRep.Update) = UserModel.Update(
        firstName = rep.firstName,
        lastName = rep.lastName
    )
}
