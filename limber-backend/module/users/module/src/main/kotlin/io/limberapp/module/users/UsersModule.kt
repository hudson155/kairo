package io.limberapp.module.users

import io.limberapp.module.Feature
import io.limberapp.module.users.endpoint.user.DeleteUser
import io.limberapp.module.users.endpoint.user.GetUser
import io.limberapp.module.users.endpoint.user.GetUserByOrgGuidAndEmailAddress
import io.limberapp.module.users.endpoint.user.GetUsersByOrgGuid
import io.limberapp.module.users.endpoint.user.PatchUser
import io.limberapp.module.users.endpoint.user.PostUser
import io.limberapp.module.users.service.user.UserService
import io.limberapp.module.users.service.user.UserServiceImpl
import io.limberapp.restInterface.EndpointHandler
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
