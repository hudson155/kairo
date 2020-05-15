package io.limberapp.backend.module.users

import com.piperframework.module.Module
import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByEmailAddress
import io.limberapp.backend.module.users.endpoint.user.GetUsersByOrgGuid
import io.limberapp.backend.module.users.endpoint.user.PatchUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.endpoint.user.role.DeleteUserRole
import io.limberapp.backend.module.users.endpoint.user.role.PutUserRole
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.AccountServiceImpl
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.module.users.service.account.UserServiceImpl
import kotlinx.serialization.modules.EmptyModule

class UsersModule : Module() {
  override val serialModule = EmptyModule

  override val endpoints = listOf(
    PostUser::class.java,
    GetUser::class.java,
    GetUserByEmailAddress::class.java,
    GetUsersByOrgGuid::class.java,
    PatchUser::class.java,
    PutUserRole::class.java,
    DeleteUserRole::class.java,
    DeleteUser::class.java
  )

  override fun bindServices() {
    bind(AccountService::class, AccountServiceImpl::class)
    bind(UserService::class, UserServiceImpl::class)
  }
}
