package io.limberapp.backend.module.users

import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByOrgGuidAndEmailAddress
import io.limberapp.backend.module.users.endpoint.user.GetUsersByOrgGuid
import io.limberapp.backend.module.users.endpoint.user.PatchUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.endpoint.user.role.DeleteUserRole
import io.limberapp.backend.module.users.endpoint.user.role.PutUserRole
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.module.users.service.account.UserServiceImpl
import io.limberapp.common.module.Module
import kotlinx.serialization.modules.EmptySerializersModule

class UsersModule : Module() {
  override val serializersModule = EmptySerializersModule

  override val endpoints = listOf(
    PostUser::class.java,
    GetUser::class.java,
    GetUserByOrgGuidAndEmailAddress::class.java,
    GetUsersByOrgGuid::class.java,
    PatchUser::class.java,
    PutUserRole::class.java,
    DeleteUserRole::class.java,
    DeleteUser::class.java
  )

  override fun bindServices() {
    bind(UserService::class, UserServiceImpl::class)
  }
}
