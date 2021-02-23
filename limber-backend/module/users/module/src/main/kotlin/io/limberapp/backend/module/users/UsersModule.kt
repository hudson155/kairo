package io.limberapp.backend.module.users

import io.limberapp.backend.module.users.endpoint.user.DeleteUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.GetUserByOrgGuidAndEmailAddress
import io.limberapp.backend.module.users.endpoint.user.GetUsersByOrgGuid
import io.limberapp.backend.module.users.endpoint.user.PatchUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.module.users.service.user.UserServiceImpl
import io.limberapp.common.module.Feature
import io.limberapp.common.restInterface.EndpointHandler
import kotlin.reflect.KClass

class UsersModule : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      PostUser::class,
      GetUser::class,
      GetUserByOrgGuidAndEmailAddress::class,
      GetUsersByOrgGuid::class,
      PatchUser::class,
      DeleteUser::class,
  )

  override fun bind() {
    bind(UserService::class.java).to(UserServiceImpl::class.java).asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
