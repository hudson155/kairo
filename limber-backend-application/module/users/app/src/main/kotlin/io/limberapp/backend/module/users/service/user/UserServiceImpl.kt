package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class UserServiceImpl @Inject constructor(
    @Store private val userStore: UserService
) : UserService by userStore
