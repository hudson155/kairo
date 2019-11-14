package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.backend.module.users.store.user.UserStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore
) : UserService {

    override fun create(entity: UserEntity.Creation) = userStore.create(entity)

    override fun get(id: UUID) = userStore.get(id)

    override fun update(id: UUID, entity: UserEntity.Update) = userStore.update(id, entity)

    override fun delete(id: UUID) = userStore.delete(id)
}
