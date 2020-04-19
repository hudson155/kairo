package io.limberapp.backend.module.auth.api.tenant.domain

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep

@Suppress("StringLiteralDuplication")
object TenantDomainApi {

    data class Post(val orgId: UUID, val rep: TenantDomainRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/tenants/${enc(orgId)}/domains",
        body = rep
    )

    data class Delete(val orgId: UUID, val domain: String) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/tenants/${enc(orgId)}/domains/${enc(domain)}"
    )
}
