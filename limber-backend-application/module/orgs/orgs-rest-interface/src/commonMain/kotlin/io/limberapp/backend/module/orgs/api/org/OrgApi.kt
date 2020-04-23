package io.limberapp.backend.module.orgs.api.org

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.orgs.rep.org.OrgRep

@Suppress("StringLiteralDuplication")
object OrgApi {

    data class Post(val rep: OrgRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/orgs",
        body = rep
    )

    data class Get(val orgId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/orgs/${enc(orgId)}"
    )

    data class GetByOwnerAccountId(val ownerAccountId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/orgs",
        queryParams = listOf("ownerAccountId" to enc(ownerAccountId))
    )

    data class Patch(val orgId: UUID, val rep: OrgRep.Update?) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/orgs/${enc(orgId)}",
        body = rep
    )

    data class Delete(val orgId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/orgs/${enc(orgId)}"
    )
}
