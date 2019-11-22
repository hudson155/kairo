package io.limberapp.backend.module.auth

import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.CreateJwtClaimsRequest
import io.limberapp.backend.module.auth.endpoint.personalAccessToken.CreatePersonalAccessToken
import io.limberapp.backend.module.auth.endpoint.personalAccessToken.DeletePersonalAccessToken
import io.limberapp.backend.module.auth.endpoint.personalAccessToken.GetPersonalAccessTokensByUserId
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestServiceImpl
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenServiceImpl
import io.limberapp.backend.module.auth.store.personalAccessToken.MongoPersonalAccessTokenStore
import io.limberapp.backend.module.auth.store.personalAccessToken.PersonalAccessTokenStore
import io.limberapp.framework.module.Module

/**
 * Authentication is performed by Auth0, so the auth module is not actually responsible for
 * authenticating users. It's also not really responsible for authorization either, since
 * authorization is handled on a per-endpoint basis regardless of which module it's in. What, might
 * you ask, does this auth module actually do, you might ask? Well, since I've already told you what
 * it doesn't do, here's what it does do.
 *
 * - The auth module is responsible for communication with Auth0. This could mean a few different
 *     things. The primary use case right now is that when users authenticate with Auth0, Auth0
 *     needs to get enough information about them to create a JWT for them. Auth0 has some of this
 *     information, but reaches out to the auth module for the rest.
 *
 * - The auth module is responsible for personal access tokens. Personal access tokens are only for
 *     admins at this time.
 */
class AuthModule : Module() {

    override val endpoints = listOf(

        CreateJwtClaimsRequest::class.java,

        CreatePersonalAccessToken::class.java,
        GetPersonalAccessTokensByUserId::class.java,
        DeletePersonalAccessToken::class.java
    )

    override fun bindServices() {
        bind(JwtClaimsRequestService::class, JwtClaimsRequestServiceImpl::class)
        bind(PersonalAccessTokenService::class, PersonalAccessTokenServiceImpl::class)
    }

    override fun bindStores() {
        bind(PersonalAccessTokenStore::class, MongoPersonalAccessTokenStore::class)
    }
}
