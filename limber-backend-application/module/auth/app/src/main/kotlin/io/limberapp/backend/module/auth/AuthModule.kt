package io.limberapp.backend.module.auth

import com.piperframework.module.Module
import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.CreateJwtClaimsRequest
import io.limberapp.backend.module.auth.endpoint.accessToken.CreateAccessToken
import io.limberapp.backend.module.auth.endpoint.accessToken.DeleteAccessToken
import io.limberapp.backend.module.auth.endpoint.accessToken.GetAccessTokensByUserId
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestServiceImpl
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenServiceImpl
import io.limberapp.backend.module.auth.store.accessToken.SqlAccessTokenStore

/**
 * Authentication is performed by Auth0, so the auth module is not actually responsible for authenticating users. It's
 * also not really responsible for authorization either, since authorization is handled on a per-endpoint basis
 * regardless of which module it's in. What, might you ask, does this auth module actually do, you might ask? Well,
 * since I've already told you what it doesn't do, here's what it does do.
 *
 * - The auth module is responsible for communication with Auth0. This could mean a few different things. The primary
 *     use case right now is that when users authenticate with Auth0, Auth0 needs to get enough information about them
 *     to create a JWT for them. Auth0 has some of this information, but reaches out to the auth module for the rest.
 *
 * - The auth module is responsible for access tokens. Access tokens are only for admins at this time.
 */
class AuthModule : Module() {

    override val endpoints = listOf(

        CreateJwtClaimsRequest::class.java,

        CreateAccessToken::class.java,
        GetAccessTokensByUserId::class.java,
        DeleteAccessToken::class.java
    )

    override fun bindServices() {
        bindService(JwtClaimsRequestService::class, JwtClaimsRequestServiceImpl::class)
        bindService(AccessTokenService::class, AccessTokenServiceImpl::class)
    }

    override fun bindStores() {
        bindStore(AccessTokenService::class, SqlAccessTokenStore::class)
    }
}
