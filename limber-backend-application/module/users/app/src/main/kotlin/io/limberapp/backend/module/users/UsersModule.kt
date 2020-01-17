package io.limberapp.backend.module.users

import com.piperframework.module.Module
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByEmailAddress
import io.limberapp.backend.module.users.endpoint.user.UpdateUser
import io.limberapp.backend.module.users.endpoint.user.role.AddUserRole
import io.limberapp.backend.module.users.endpoint.user.role.RemoveUserRole
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.module.users.service.user.UserServiceImpl
import io.limberapp.backend.module.users.store.user.SqlUserStore
import io.limberapp.backend.module.users.store.user.UserStore

/**
 * The users module contains the basics of a user of the platform. Be careful not to overload this module with too much
 * information about a user. Technically, almost everything could be related back to the user and you could make an
 * argument to put it in this module, but the intention is to keep this module as slim as possible.
 */
class UsersModule : Module() {

    override val endpoints = listOf(

        CreateUser::class.java,
        GetUser::class.java,
        GetUserByEmailAddress::class.java,
        UpdateUser::class.java,
        AddUserRole::class.java,
        RemoveUserRole::class.java,
        DeleteUser::class.java
    )

    override fun bindServices() {
        bind(UserService::class, UserServiceImpl::class)
    }

    override fun bindStores() {
        bind(UserStore::class, SqlUserStore::class)
    }
}
