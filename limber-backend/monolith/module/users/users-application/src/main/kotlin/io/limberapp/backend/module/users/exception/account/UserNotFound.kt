package io.limberapp.backend.module.users.exception.account

import com.piperframework.exception.exception.notFound.EntityNotFound

internal class UserNotFound : EntityNotFound("User")
