package io.limberapp.backend.module.users

import com.piperframework.module.Module
import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByEmailAddress
import io.limberapp.backend.module.users.endpoint.user.PatchUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.endpoint.user.role.DeleteUserRole
import io.limberapp.backend.module.users.endpoint.user.role.PutUserRole
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.AccountServiceImpl
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.module.users.service.account.UserServiceImpl
import io.limberapp.backend.module.users.store.account.AccountStore
import io.limberapp.backend.module.users.store.account.SqlAccountMapper
import io.limberapp.backend.module.users.store.account.SqlAccountMapperImpl
import io.limberapp.backend.module.users.store.account.SqlAccountStore
import io.limberapp.backend.module.users.store.account.SqlUserStore
import io.limberapp.backend.module.users.store.account.UserStore

class UsersModule : Module() {

    override val endpoints = listOf(

        PostUser::class.java,
        GetUser::class.java,
        GetUserByEmailAddress::class.java,
        PatchUser::class.java,
        PutUserRole::class.java,
        DeleteUserRole::class.java,
        DeleteUser::class.java
    )

    override fun bindServices() {
        bind(AccountService::class, AccountServiceImpl::class)
        bind(UserService::class, UserServiceImpl::class)
    }

    override fun bindStores() {
        bind(SqlAccountMapper::class, SqlAccountMapperImpl::class)
        bind(AccountStore::class, SqlAccountStore::class)
        bind(UserStore::class, SqlUserStore::class)
    }
}
