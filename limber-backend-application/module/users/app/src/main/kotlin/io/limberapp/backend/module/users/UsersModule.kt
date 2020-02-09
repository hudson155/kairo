package io.limberapp.backend.module.users

import com.piperframework.module.Module
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByEmailAddress
import io.limberapp.backend.module.users.endpoint.user.UpdateUser
import io.limberapp.backend.module.users.endpoint.user.role.AddUserRole
import io.limberapp.backend.module.users.endpoint.user.role.RemoveUserRole
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

        CreateUser::class.java,
        GetUser::class.java,
        GetUserByEmailAddress::class.java,
        UpdateUser::class.java,
        AddUserRole::class.java,
        RemoveUserRole::class.java,
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
