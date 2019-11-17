package io.limberapp.backend.module.auth

import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.CreateJwtClaimsRequest
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestServiceImpl
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.command.AbstractCommand
import io.limberapp.framework.module.Module

/**
 * Authentication is performed by Auth0, so the auth module is not actually responsible for
 * authenticating users. It's also not really responsible for authorization either, since
 * authorization is handled on a per-endpoint basis regardless of which module it's in. What, might
 * you ask, does this auth module actually do, you might ask? Well, since I've already told you what
 * it doesn't do, here's what it does do. The auth module is responsible for communication with
 * Auth0. This could mean a few different things. The primary use case right now is that when users
 * authenticate with Auth0, Auth0 needs to get enough information about them to create a JWT for
 * them. Auth0 has some of this information, but reaches out to the auth module for the rest.
 */
class AuthModule : Module() {

    /**
     * TODO: The explicit type argument is only needed because there's a single endpoint. If/when
     *  there are more, it won't be necessary.
     */
    override val endpoints = listOf<Class<out ApiEndpoint<out AbstractCommand, out Any?>>>(
        CreateJwtClaimsRequest::class.java
    )

    override fun bindServices() {
        bind(JwtClaimsRequestService::class, JwtClaimsRequestServiceImpl::class)
    }

    override fun bindStores() = Unit
}
