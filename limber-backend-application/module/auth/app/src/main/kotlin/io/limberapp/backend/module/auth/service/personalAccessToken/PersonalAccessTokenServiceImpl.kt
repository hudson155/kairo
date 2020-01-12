package io.limberapp.backend.module.auth.service.personalAccessToken

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class PersonalAccessTokenServiceImpl @Inject constructor(
    @Store private val personalAccessTokenStore: PersonalAccessTokenService
) : PersonalAccessTokenService by personalAccessTokenStore
