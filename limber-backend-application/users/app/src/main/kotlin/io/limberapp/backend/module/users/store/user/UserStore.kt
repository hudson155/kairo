package io.limberapp.backend.module.users.store.user

import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.store.Store

internal interface UserStore : Store<UserEntity, UserEntity.Update> {

    fun getByEmailAddress(emailAddress: String): UserEntity?
}
