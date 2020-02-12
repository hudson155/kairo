package io.limberapp.backend.module.auth

import com.piperframework.module.Module
import io.limberapp.backend.module.auth.endpoint.account.accessToken.DeleteAccessToken
import io.limberapp.backend.module.auth.endpoint.account.accessToken.GetAccessTokensByAccountId
import io.limberapp.backend.module.auth.endpoint.account.accessToken.PostAccessToken
import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.PostJwtClaimsRequest
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenServiceImpl
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestServiceImpl
import io.limberapp.backend.module.auth.store.accessToken.AccessTokenStore
import io.limberapp.backend.module.auth.store.accessToken.SqlAccessTokenMapper
import io.limberapp.backend.module.auth.store.accessToken.SqlAccessTokenMapperImpl
import io.limberapp.backend.module.auth.store.accessToken.SqlAccessTokenStore
import io.limberapp.backend.module.auth.store.tenant.SqlTenantMapper
import io.limberapp.backend.module.auth.store.tenant.SqlTenantMapperImpl
import io.limberapp.backend.module.auth.store.tenant.SqlTenantStore
import io.limberapp.backend.module.auth.store.tenant.TenantStore

class AuthModule : Module() {

    override val endpoints = listOf(

        PostJwtClaimsRequest::class.java,

        PostAccessToken::class.java,
        GetAccessTokensByAccountId::class.java,
        DeleteAccessToken::class.java
    )

    override fun bindServices() {
        bind(JwtClaimsRequestService::class, JwtClaimsRequestServiceImpl::class)
        bind(AccessTokenService::class, AccessTokenServiceImpl::class)
    }

    override fun bindStores() {

        bind(SqlTenantMapper::class, SqlTenantMapperImpl::class)
        bind(TenantStore::class, SqlTenantStore::class)

        bind(SqlAccessTokenMapper::class, SqlAccessTokenMapperImpl::class)
        bind(AccessTokenStore::class, SqlAccessTokenStore::class)
    }
}
