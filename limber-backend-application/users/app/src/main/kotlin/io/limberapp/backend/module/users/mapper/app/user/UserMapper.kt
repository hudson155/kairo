package io.limberapp.backend.module.users.mapper.app.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.backend.module.users.model.user.UserModel

internal class UserMapper @Inject constructor() {

    fun entity(model: UserModel) = UserEntity(
        id = model.id,
        created = model.created,
        version = model.version,
        firstName = model.firstName,
        lastName = model.lastName,
        emailAddress = model.emailAddress,
        profilePhotoUrl = model.profilePhotoUrl,
        roles = model.roles
    )

    fun model(entity: UserEntity) = UserModel(
        id = entity.id,
        created = entity.created,
        version = entity.version,
        firstName = entity.firstName,
        lastName = entity.lastName,
        emailAddress = entity.emailAddress,
        profilePhotoUrl = entity.profilePhotoUrl,
        roles = entity.roles
    )

    fun update(model: UserModel.Update) = UserEntity.Update(
        firstName = model.firstName,
        lastName = model.lastName
    )
}
