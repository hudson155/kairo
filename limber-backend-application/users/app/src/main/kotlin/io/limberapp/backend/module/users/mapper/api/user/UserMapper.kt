package io.limberapp.backend.module.users.mapper.api.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class UserMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun creationEntity(rep: UserRep.Creation) = UserEntity.Creation(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        version = 0,
        firstName = rep.firstName,
        lastName = rep.lastName,
        emailAddress = rep.emailAddress,
        profilePhotoUrl = rep.profilePhotoUrl
    )

    fun completeRep(entity: UserEntity.Complete) = UserRep.Complete(
        id = entity.id,
        created = entity.created,
        firstName = entity.firstName,
        lastName = entity.lastName,
        emailAddress = entity.emailAddress,
        profilePhotoUrl = entity.profilePhotoUrl
    )

    fun updateEntity(rep: UserRep.Update) = UserEntity.Update(
        firstName = rep.firstName,
        lastName = rep.lastName
    )
}
