package io.limberapp.backend.module.auth.service.accessToken

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class AccessTokenServiceImpl @Inject constructor(
    @Store private val accessTokenStore: AccessTokenService
) : AccessTokenService by accessTokenStore
