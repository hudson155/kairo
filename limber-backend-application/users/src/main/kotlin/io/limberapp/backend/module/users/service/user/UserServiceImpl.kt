package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore
) : UserService {

    override fun create(model: UserModel.Creation) = userStore.create(model)

    override fun get(id: UUID) = userStore.get(id)

    override fun update(id: UUID, model: UserModel.Update) = userStore.update(id, model)

    override fun delete(id: UUID) = userStore.delete(id)
}
