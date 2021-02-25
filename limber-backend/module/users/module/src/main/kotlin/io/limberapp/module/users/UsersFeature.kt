package io.limberapp.module.users

import io.limberapp.endpoint.user.DeleteUser
import io.limberapp.endpoint.user.GetUser
import io.limberapp.endpoint.user.GetUserByOrgGuidAndEmailAddress
import io.limberapp.endpoint.user.GetUsersByOrgGuid
import io.limberapp.endpoint.user.PatchUser
import io.limberapp.endpoint.user.PostUser
import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.service.user.UserService
import io.limberapp.service.user.UserServiceImpl
import kotlin.reflect.KClass

class UsersFeature : Feature() {
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
