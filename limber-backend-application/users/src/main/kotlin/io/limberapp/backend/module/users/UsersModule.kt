package io.limberapp.backend.module.users

import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.module.users.service.user.UserServiceImpl
import io.limberapp.backend.module.users.store.user.MongoUserStore
import io.limberapp.backend.module.users.store.user.UserStore
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.command.AbstractCommand
import io.limberapp.framework.module.Module

/**
 * The users module contains the basics of a user of the platform. Be careful not to overload this
 * module with too much information about a user. Technically, almost everything could be related
 * back to the org and you could make an argument to put it in this module, but the intention is to
 * keep this module as slim as possible.
 */
class UsersModule : Module() {

    override val endpoints = emptyList<Class<out ApiEndpoint<out AbstractCommand, out Any?>>>()

    override fun bindServices() {
        bind(UserService::class, UserServiceImpl::class)
    }

    override fun bindStores() {
        bind(UserStore::class, MongoUserStore::class)
    }
}
