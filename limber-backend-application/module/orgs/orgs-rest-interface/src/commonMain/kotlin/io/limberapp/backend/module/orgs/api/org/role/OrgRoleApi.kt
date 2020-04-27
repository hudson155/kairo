package io.limberapp.backend.module.orgs.api.org.role

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.orgs.rep.org.OrgRoleRep

@Suppress("StringLiteralDuplication")
object OrgRoleApi {
    data class Post(val orgGuid: UUID, val rep: OrgRoleRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/orgs/${enc(orgGuid)}/roles",
        body = rep
    )
}
