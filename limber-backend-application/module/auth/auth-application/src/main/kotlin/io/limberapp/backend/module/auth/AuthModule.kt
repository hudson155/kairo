package io.limberapp.backend.module.auth

import com.piperframework.module.Module
import io.limberapp.backend.module.auth.endpoint.account.accessToken.DeleteAccessToken
import io.limberapp.backend.module.auth.endpoint.account.accessToken.GetAccessTokensByAccountGuid
import io.limberapp.backend.module.auth.endpoint.account.accessToken.PostAccessToken
import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.PostJwtClaimsRequest
import io.limberapp.backend.module.auth.endpoint.tenant.DeleteTenant
import io.limberapp.backend.module.auth.endpoint.tenant.GetTenant
import io.limberapp.backend.module.auth.endpoint.tenant.GetTenantByDomain
import io.limberapp.backend.module.auth.endpoint.tenant.PatchTenant
import io.limberapp.backend.module.auth.endpoint.tenant.PostTenant
import io.limberapp.backend.module.auth.endpoint.tenant.domain.DeleteTenantDomain
import io.limberapp.backend.module.auth.endpoint.tenant.domain.PostTenantDomain
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenServiceImpl
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestServiceImpl
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantDomainServiceImpl
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.auth.service.tenant.TenantServiceImpl
import kotlinx.serialization.modules.EmptyModule

class AuthModule : Module() {
    override val serialModule = EmptyModule

    override val endpoints = listOf(
        PostTenant::class.java,
        GetTenant::class.java,
        GetTenantByDomain::class.java,
        PatchTenant::class.java,
        DeleteTenant::class.java,
        PostTenantDomain::class.java,
        DeleteTenantDomain::class.java,

        PostJwtClaimsRequest::class.java,

        PostAccessToken::class.java,
        GetAccessTokensByAccountGuid::class.java,
        DeleteAccessToken::class.java
    )

    override fun bindServices() {
        bind(TenantService::class, TenantServiceImpl::class)
        bind(TenantDomainService::class, TenantDomainServiceImpl::class)

        bind(JwtClaimsRequestService::class, JwtClaimsRequestServiceImpl::class)

        bind(AccessTokenService::class, AccessTokenServiceImpl::class)
    }
}
