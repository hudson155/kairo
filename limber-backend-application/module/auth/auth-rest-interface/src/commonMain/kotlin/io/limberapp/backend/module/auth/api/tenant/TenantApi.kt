package io.limberapp.backend.module.auth.api.tenant

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.tenant.TenantRep

@Suppress("StringLiteralDuplication")
object TenantApi {

    data class Post(val rep: TenantRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/tenants",
        body = rep
    )

    data class Get(val orgId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/tenants/${enc(orgId)}"
    )

    data class GetByDomain(val domain: String) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/tenants",
        queryParams = listOf("domain" to enc(domain))
    )

    data class Patch(val orgId: UUID, val rep: TenantRep.Update?) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/tenants/${enc(orgId)}",
        body = rep
    )

    data class Delete(val orgId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/tenants/${enc(orgId)}"
    )
}
