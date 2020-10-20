package io.limberapp.backend.module.users

import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByOrgGuidAndEmailAddress
import io.limberapp.backend.module.users.endpoint.user.GetUsersByOrgGuid
import io.limberapp.backend.module.users.endpoint.user.PatchUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.module.users.service.account.UserServiceImpl
import io.limberapp.common.module.ApplicationModule

class UsersModule : ApplicationModule() {
  override val endpoints = listOf(
      PostUser::class.java,
      GetUser::class.java,
      GetUserByOrgGuidAndEmailAddress::class.java,
      GetUsersByOrgGuid::class.java,
      PatchUser::class.java,
      DeleteUser::class.java
  )

  override fun bindServices() {
    bind(UserService::class.java).to(UserServiceImpl::class.java).asEagerSingleton()
  }
}
