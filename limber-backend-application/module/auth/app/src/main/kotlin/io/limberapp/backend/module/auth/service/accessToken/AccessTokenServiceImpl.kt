package io.limberapp.backend.module.auth.service.accessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.store.accessToken.AccessTokenStore

internal class AccessTokenServiceImpl @Inject constructor(
    accessTokenStore: AccessTokenStore
) : AccessTokenService by accessTokenStore
