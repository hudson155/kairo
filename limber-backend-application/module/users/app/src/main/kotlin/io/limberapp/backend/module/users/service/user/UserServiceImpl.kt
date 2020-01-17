package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.store.user.UserStore

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore
) : UserService by userStore
